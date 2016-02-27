/**
 * Created by RK on 20.02.16.
 */

applicationSpec.sources.create( platform.name, HaxeSourceSet.class );

if( platform.name != HaxePlatformType.HAXE.toString() )
	applicationSpec.sources.create( HaxePlatformType.HAXE.toString(), HaxeSourceSet.class );


						// ----------------------- //

						for( LanguageSourceSet languageSourceSet : applicationSpecInternal.sources )
						{
							if( languageSourceSet instanceof HaxeSourceSetInternal )
							{
								HaxeSourceSetInternal sourceSetInternal = (HaxeSourceSetInternal) languageSourceSet;
								binarySpecInternal.sources.put( sourceSetInternal.name, sourceSetInternal );
							}
						}

	//					binarySpecInternal.getSources().create( platform.name, HaxeSourceSet.class )
	//					{
	//						getSource().srcDir("src");
	//						getSource().include("**/*.hx");
	//					}