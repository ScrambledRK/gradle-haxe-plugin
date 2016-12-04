package at.dotpoint.gradle.cross.specification;

import at.dotpoint.gradle.cross.options.model.IOptions;
import org.gradle.platform.base.SourceComponentSpec;

/**
 * Created by RK on 2016-11-13.
 */
public interface ITestComponentSpec extends SourceComponentSpec
{
	//
	IOptions getOptions();

	//
	String getMain();
}
