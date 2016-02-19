package at.dotpoint.gradle

import at.dotpoint.gradle.model.DefaultHaxeApplicationBinarySpec
import at.dotpoint.gradle.model.DefaultHaxeApplicationSpec
import at.dotpoint.gradle.model.DefaultHaxePlatformAwareSpec
import at.dotpoint.gradle.model.HaxeApplicationBinarySpec
import at.dotpoint.gradle.model.HaxeApplicationBinarySpecInternal
import at.dotpoint.gradle.model.HaxeApplicationSpecInternal
import at.dotpoint.gradle.model.HaxeSourceSet
import at.dotpoint.gradle.model.HaxeApplicationSpec
import at.dotpoint.gradle.model.HaxePlatformAwareSpec
import at.dotpoint.gradle.model.HaxePlatformAwareSpecInternal
import at.dotpoint.gradle.platform.HaxePlatformResolver
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.reflect.Instantiator
import org.gradle.model.Defaults
import org.gradle.model.Each
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.platform.base.BinaryType
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder
import org.gradle.platform.base.internal.PlatformResolvers

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
        public void registerPlatformResolver( PlatformResolvers platformResolvers )
        {
           platformResolvers.register( new HaxePlatformResolver() );
        }

        @Mutate
        void createDefaultHaxeApplication( ModelMap<HaxeApplicationSpec> builder )
        {
            builder.create("libraries");
            builder.create("documentations");
            builder.create("tests");
        }

        @Defaults
        void createHaxeSourceSet( @Each HaxeApplicationSpec haxeApplication )
        {
            haxeApplication.getSources().create( "haxe", HaxeSourceSet.class )
            {
                getSource().srcDir("src");
                getSource().include("**/*.hx");
            }
        }

    }

}
