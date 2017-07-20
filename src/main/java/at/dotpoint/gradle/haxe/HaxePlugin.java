package at.dotpoint.gradle.haxe;

import at.dotpoint.gradle.cross.CrossPlugin;
import at.dotpoint.gradle.cross.transform.builder.ITransformationBuilder;
import at.dotpoint.gradle.cross.transform.builder.lifecycle.LifeCycleTransformationBuilder;
import at.dotpoint.gradle.cross.transform.container.LifeCycleTransformationContainer;
import at.dotpoint.gradle.cross.transform.repository.ITransformBuilderRepository;
import at.dotpoint.gradle.haxe.transform.java.JavaTransformation;
import at.dotpoint.gradle.haxe.transform.javascript.JavascriptTransformation;
import at.dotpoint.gradle.haxe.transform.neko.NekoTransformation;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;

import javax.inject.Inject;
/**
 *  Created by RK on 28.03.2016.
 */
public class HaxePlugin implements Plugin<Project>
{

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

	// ---------------------------------------------------------- //
	// ---------------------------------------------------------- //

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
}
