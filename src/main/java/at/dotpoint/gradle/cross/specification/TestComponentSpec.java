package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.options.model.IOptions;
import at.dotpoint.gradle.cross.options.model.Options;
import org.gradle.language.base.LanguageSourceSet;
import org.gradle.model.ModelMap;
import org.gradle.model.internal.core.ModelMaps;
import org.gradle.model.internal.core.MutableModelNode;
import org.gradle.model.internal.type.ModelType;
import org.gradle.platform.base.component.internal.DefaultComponentSpec;

/**
 * Created by RK on 2016-11-13.
 */
public class TestComponentSpec extends DefaultComponentSpec implements ITestComponentSpecInternal
{
	//
	private static final ModelType<LanguageSourceSet> LANGUAGE_SOURCE_SET_MODEL_TYPE = ModelType.of(LanguageSourceSet.class);
	private final MutableModelNode sources;

	//
	protected IOptions configuration;
	protected String main;

	// ------------------------------------------------------------------ //
	// ------------------------------------------------------------------ //

	public TestComponentSpec()
	{
		this.sources = ModelMaps.addModelMapNode( getInfo().modelNode, LANGUAGE_SOURCE_SET_MODEL_TYPE, "sources");
	}

	// ------------------------------------------------------------------ //
	// ------------------------------------------------------------------ //

	/**
	 *
	 * @return
	 */
	public IOptions getOptions()
	{
		if( this.configuration == null )
			this.configuration = new Options();

		return this.configuration;
	}

	@Override
	public void setOptions( IOptions configuration )
		{
			this.configuration = configuration;
		}

	@Override
	public String getMain()
	{
		return this.main;
	}

	public void setMain( String main )
	{
		this.main = main;
	}

	/**
	 * The source sets for this component.
	 */
	@Override
	public ModelMap<LanguageSourceSet> getSources()
	{
		return ModelMaps.toView(sources, LANGUAGE_SOURCE_SET_MODEL_TYPE);
	}
}
