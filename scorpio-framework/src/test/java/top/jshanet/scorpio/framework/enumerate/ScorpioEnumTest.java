package top.jshanet.scorpio.framework.enumerate;

import org.junit.Test;
import top.jshanet.scorpio.framework.util.JsonMapper;

/**
 * @author seanjiang
 * @since 2020-07-23
 */
public class ScorpioEnumTest {

    private static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();

    public enum TestEnum implements ScorpioEnum {
        @Description("test") TEST;
    }

    @Test
    public void testDeserialize() {
        System.out.println(JSON_MAPPER.toJson(TestEnum.TEST));
        System.out.println(TestEnum.TEST.construct(0));
    }
}
