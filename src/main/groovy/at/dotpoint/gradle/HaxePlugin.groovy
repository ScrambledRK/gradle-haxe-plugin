package at.dotpoint.gradle

import at.dotpoint.gradle.model.DefaultHaxeApplicationBinarySpec
import at.dotpoint.gradle.model.DefaultHaxeApplicationSpec
import at.dotpoint.gradle.model.DefaultHaxePlatformAwareSpec
import at.dotpoint.gradle.model.DefaultHaxeReleaseFlavor
import at.dotpoint.gradle.model.HaxeReleaseFlavor
import at.dotpoint.gradle.model.HaxeReleaseFlavorType
import at.dotpoint.gradle.sourceset.DefaultHaxeSourceSet
import at.dotpoint.gradle.model.HaxeApplicationBinarySpec
import at.dotpoint.gradle.model.HaxeApplicationBinarySpecInternal
import at.dotpoint.gradle.model.HaxeApplicationSpecInternal
import at.dotpoint.gradle.sourceset.HaxeLanguageTransform
import at.dotpoint.gradle.sourceset.HaxeSourceSet
import at.dotpoint.gradle.model.HaxeApplicationSpec
import at.dotpoint.gradle.model.HaxePlatformAwareSpec
import at.dotpoint.gradle.model.HaxePlatformAwareSpecInternal
import at.dotpoint.gradle.sourceset.HaxeSourceSetInternal
import at.dotpoint.gradle.platform.DefaultHaxePlatform
import at.dotpoint.gradle.platform.HaxePlatform
import at.dotpoint.gradle.platform.HaxePlatformResolver
import at.dotpoint.gradle.platform.HaxePlatformType
import at.dotpoint.gradle.tasks.ConvertSourceTask
import at.dotpoint.gradle.util.StringUtil
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.reflect.Instantiator
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.base.ProjectSourceSet
import org.gradle.language.base.internal.registry.LanguageTransformContainer
import org.gradle.model.Defaults
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.model.internal.manage.schema.ModelSchemaStore
import org.gradle.platform.base.BinaryType
import org.gradle.platform.base.ComponentBinaries
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.LanguageType
import org.gradle.platform.base.Platform
import org.gradle.platform.base.PlatformContainer
import org.gradle.platform.base.TypeBuilder
import org.gradle.platform.base.internal.PlatformRequirement
import org.gradle.platform.base.internal.PlatformResolvers
import org.gradle.tooling.model.Task

import javax.inject.Inject

/**
 * Created by RK on 12.02.16.
 */
class HaxePlugin implements Plugin<Project>
{
    /**
     *
     */
    private HaxeExtension extension;

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
        this.extension = this.createExtension( project );
    }

    // ---------------------------------------------------------- //
    // ---------------------------------------------------------- //

    private HaxeExtension createExtension( Project project )
    {
        return project.extensions.create( HaxeExtension.NAME, HaxeExtension, project, this );

        project.extensions.extraProperties.set( "HaxeSourceSet", HaxeSourceSet );
    }

    // ---------------------------------------------------------- //
    // ---------------------------------------------------------- //

    /**
     *
     */
    @SuppressWarnings("UnusedDeclaration")
    //
    static class HaxePluginRules extends RuleSource
    {
        /**
         * LanguageSourceSet
         */
        @LanguageType
        void registerSourceSet( TypeBuilder<HaxeSourceSet> builder )
		{
			builder.defaultImplementation(DefaultHaxeSourceSet.class);
		    builder.internalView(HaxeSourceSetInternal.class);
        }

        /**
         * BinarySpec
         */
        @BinaryType
        void registerApplicationBinarySpec( TypeBuilder<HaxeApplicationBinarySpec> builder )
        {
           builder.defaultImplementation(DefaultHaxeApplicationBinarySpec.class);
           builder.internalView(HaxeApplicationBinarySpecInternal.class);
        }

        /**
         * PlatformSpec
         */
        @ComponentType
        void registerPlatformAwareSpec( TypeBuilder<HaxePlatformAwareSpec> builder )
        {
            builder.defaultImplementation(DefaultHaxePlatformAwareSpec.class);
            builder.internalView(HaxePlatformAwareSpecInternal.class);
        }

        /**
         * ApplicationSpec
         */
        @ComponentType
        void registerApplicationSpec( TypeBuilder<HaxeApplicationSpec> builder )
        {
            builder.defaultImplementation(DefaultHaxeApplicationSpec.class);
            builder.internalView(HaxeApplicationSpecInternal.class);
        }

        // -------------------------------------------------- //
        // -------------------------------------------------- //

        /**
         * PlatformResolver
         */
        @Mutate
        public void registerPlatformResolver( PlatformResolvers platformResolvers, PlatformContainer platformContainer  )
        {
           platformResolvers.register( new HaxePlatformResolver( platformContainer ) );
        }

        /**
         * DefaultPlatforms
         */
        @Mutate
        void createDefaultPlatforms( PlatformContainer platformContainer )
        {
            for( HaxePlatformType type : HaxePlatformType.values() )
            {
                platformContainer.add( new DefaultHaxePlatform( type ) );
            }
        }

        /**
         * DefaultApplications
         */
        @Mutate
        void createDefaultApplications( ModelMap<HaxeApplicationSpec> builder, PlatformContainer platformContainer )
        {
			builder.create( "docs" ){ HaxeApplicationSpec applicationSpec ->

				platformContainer.all{ Platform platform ->
					applicationSpec.platform( platform.name );
				}

			}
        }

        @ComponentBinaries
        void generateApplicationBinaries( ModelMap<HaxeApplicationBinarySpec> builder, HaxeApplicationSpec applicationSpec, PlatformResolvers platformResolver )
        {
            HaxeApplicationSpecInternal applicationSpecInternal = (HaxeApplicationSpecInternal) applicationSpec;

			//
            for( PlatformRequirement platformRequirement : applicationSpecInternal.getTargetPlatforms() )
            {
                HaxePlatform platform = platformResolver.resolve( HaxePlatform, platformRequirement );

				for( HaxeReleaseFlavorType flavor : HaxeReleaseFlavorType.values() )
				{
					builder.create( StringUtil.toCamelCase( platform.name, flavor.toString() ) ){ HaxeApplicationBinarySpec binarySpec ->

						HaxeApplicationBinarySpecInternal binarySpecInternal = (HaxeApplicationBinarySpecInternal) binarySpec;

						binarySpecInternal.setTargetPlatform( platform );
						binarySpecInternal.setReleaseType( new DefaultHaxeReleaseFlavor( flavor ) );
					}
				}
            }
        }

		@Mutate
		void registerLanguageTransform(LanguageTransformContainer languages, ServiceRegistry serviceRegistry)
		{
			ModelSchemaStore schemaStore = serviceRegistry.get(ModelSchemaStore.class);
			languages.add( new HaxeLanguageTransform(schemaStore) );
		}

    }

}
