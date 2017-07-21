package at.dotpoint.gradle.haxe;

import at.dotpoint.gradle.cross.CrossPlugin;
import at.dotpoint.gradle.cross.sourceset.ISourceSet;
import at.dotpoint.gradle.cross.specification.IApplicationComponentSpec;
import at.dotpoint.gradle.cross.transform.builder.ITransformationBuilder;
import at.dotpoint.gradle.cross.transform.builder.lifecycle.LifeCycleTransformationBuilder;
import at.dotpoint.gradle.cross.transform.container.LifeCycleTransformationContainer;
import at.dotpoint.gradle.cross.transform.repository.ITransformBuilderRepository;
import at.dotpoint.gradle.cross.variant.container.flavor.IFlavorContainer;
import at.dotpoint.gradle.cross.variant.container.platform.IPlatformContainer;
import at.dotpoint.gradle.cross.variant.model.flavor.IFlavor;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatform;
import at.dotpoint.gradle.cross.variant.model.platform.IPlatformInternal;
import at.dotpoint.gradle.haxe.transform.java.JavaTransformation;
import at.dotpoint.gradle.haxe.transform.javascript.JavascriptTransformation;
import at.dotpoint.gradle.haxe.transform.neko.NekoTransformation;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.model.*;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;

/**
 *  Created by RK on 28.03.2016.
 */
public class HaxePlugin implements Plugin<Project>
{
	//
	private static final Logger LOGGER = Logging.getLogger(HaxePlugin.class);

	/**
	 *
	 */
	public final Instantiator instantiator;
	public final FileResolver fileResolver;

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

	@Inject
	public HaxePlugin( Instantiator instantiator, FileResolver fileResolver )
	{
		this.instantiator = instantiator;
		this.fileResolver = fileResolver;
	}

	@Override
	public void apply( final Project project )
	{
		project.getPluginManager().apply( CrossPlugin.class );
	}

	// ********************************************************************************************** //
	// ********************************************************************************************** //

	/**
	 *
	 */
	static class HaxePluginRules extends RuleSource
	{

		// -------------------------------------------------- //
		// -------------------------------------------------- //

		@Mutate
		void registerTransformBuilder( final ITransformBuilderRepository transforms,
		                               ServiceRegistry serviceRegistry )
		{
			transforms.add( this.createLifeCycleTransformBuilder( serviceRegistry ) );
		}

		/**
		 */
		private ITransformationBuilder createLifeCycleTransformBuilder( ServiceRegistry serviceRegistry )
		{
			LifeCycleTransformationContainer container = new LifeCycleTransformationContainer();

			container.add( new JavaTransformation( serviceRegistry ) );
			container.add( new NekoTransformation( serviceRegistry ) );
			container.add( new JavascriptTransformation( serviceRegistry ) );

			return new LifeCycleTransformationBuilder( container );
		}
	}
	
	// ********************************************************************************************** //
	// ********************************************************************************************** //

	/**
	 *
	 */
	static class DefaultConfigurationRules extends RuleSource
	{
		//
		private static final File SRC_MAIN = new File( "src/main/" );
		private static final File SRC_UNIT = new File( "src/test/" );
		
		//
		@Defaults
		void generateDefaults( ModelMap<IApplicationComponentSpec> builder,
		                       IPlatformContainer platformContainer,
		                       IFlavorContainer flavorContainer,
		                       ServiceRegistry serviceRegistry )
		{
			LOGGER.info( "generateDefaults: {}", builder );
			
			this.generatePlatformDirectories( platformContainer, serviceRegistry.get( FileResolver.class ) );
			this.generateApplicationComponents( builder, platformContainer, flavorContainer );
		}
		
		//
		private void generatePlatformDirectories( IPlatformContainer platformContainer,
		                                          FileResolver fileResolver )
		{
			LOGGER.info( "-generatePlatformDirectories" );
			
			//
			for( IPlatform platform : platformContainer )
			{
				IPlatformInternal platformInternal = (IPlatformInternal) platform;
				IPlatformInternal referencePlatform = HaxePlatforms.getInstance()
						.getPlatformByName( platformInternal.getName() );
				
				if( referencePlatform != null && platformInternal.getDirectories() == null )
					platformInternal.setDirectories( new ArrayList<>( referencePlatform.getDirectories() ) );
				
				if( platformInternal.getMainSourceDirectory() == null )
					platformInternal.setMainSourceDirectory( SRC_MAIN );
				
				if( platformInternal.getUnitSourceDirectory() == null )
					platformInternal.setUnitSourceDirectory( SRC_UNIT );
				
				File main = platformInternal.getMainSourceDirectory();
				File unit = platformInternal.getUnitSourceDirectory();
				
				main = fileResolver.resolve( main );
				unit = fileResolver.resolve( unit );
				
				platformInternal.setMainSourceDirectory( main );
				platformInternal.setUnitSourceDirectory( unit);
				
				LOGGER.info( "--sourcePlatform {} with {}", platform, platform.getDirectories() );
			}
		}
		
		//
		private void generateApplicationComponents( ModelMap<IApplicationComponentSpec> builder,
		                                            IPlatformContainer platformContainer,
		                                            IFlavorContainer flavorContainer )
		{
			LOGGER.info( "-generateApplicationComponents" );
			
			//
			builder.create( "main", ( IApplicationComponentSpec main ) ->
			{
				LOGGER.info( "--create IApplicationComponentSpec: {}", main );
				
				for( IFlavor flavor : flavorContainer )
					main.buildType( flavor.getName() );
				
				for( IPlatform platform : platformContainer )
					main.platform( platform.getName() );
			} );
		}
		
		//
		@Mutate
		void generateSourceSets( @Each IApplicationComponentSpec applicationComponentSpec,
		                         IPlatformContainer platformContainer )
		{
			LOGGER.info( "generateSourceSets: {}", applicationComponentSpec );
			
			//
			for( IPlatform platform : platformContainer )
			{
				applicationComponentSpec.getSources().create( platform.getName(), ISourceSet.class, sourceSet ->
				{
					LOGGER.info( "-create SourceSet for {}", platform );
					
					//
					for( String srcName : platform.getDirectories() )
					{
						File sourceDirectory = new File( platform.getMainSourceDirectory(), srcName );
						sourceSet.getSource().srcDirs( sourceDirectory );
					}
					
					//
					sourceSet.getSource().include( "**/*.hx" );
					
					sourceSet.sourcePlatform( "haxe" );
					sourceSet.targetPlatform( platform.getName() );
				} );
			}
		}
	}
}
