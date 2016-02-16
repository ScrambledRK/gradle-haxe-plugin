package at.dotpoint.gradle

import at.dotpoint.gradle.model.DefaultHaxeTargetPlatformSpec
import at.dotpoint.gradle.model.HaxeApplicationSpecInternal
import at.dotpoint.gradle.model.HaxeSourceSet
import at.dotpoint.gradle.model.HaxeApplicationSpec
import at.dotpoint.gradle.model.HaxeTargetPlatformSpec
import at.dotpoint.gradle.model.HaxeTargetPlatformSpecInternal
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.reflect.Instantiator
import org.gradle.model.Defaults
import org.gradle.model.Each
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder

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

        project.extensions.extraProperties.set( "HaxeTargetPlatformSpec", HaxeTargetPlatformSpec );
        project.extensions.extraProperties.set( "HaxeSourceSet", HaxeSourceSet );
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
    static class TargetPlatformRule extends RuleSource
    {

        @ComponentType
        void registerHaxeTargetPlatform( TypeBuilder<HaxeTargetPlatformSpec> builder )
        {
            builder.defaultImplementation(DefaultHaxeTargetPlatformSpec.class);
            builder.internalView(HaxeTargetPlatformSpecInternal.class);
        }

        @ComponentType
        void registerHaxeApplicationSpec( TypeBuilder<HaxeApplicationSpec> builder ) {
            builder.internalView(HaxeApplicationSpecInternal.class);
        }

        @Mutate
        void createDefaultHaxeApplication( ModelMap<HaxeApplicationSpec> builder ) {
            builder.create("haxe");
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
