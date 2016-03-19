package at.dotpoint.gradle.cross.specification

import org.gradle.internal.HasInternalProtocol
import org.gradle.model.Managed
import org.gradle.platform.base.GeneralComponentSpec

/**
 * Created by RK on 11.03.16.
 */
@HasInternalProtocol
//
public interface IGeneralComponentSpec extends GeneralComponentSpec {

}

/**
 * Created by RK on 11.03.16.
 */
@Managed
//
public interface IGeneralComponentSpecInternal extends IGeneralComponentSpec {

}