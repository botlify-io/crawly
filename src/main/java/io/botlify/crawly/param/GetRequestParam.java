package io.botlify.crawly.param;

import lombok.Builder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Builder(setterPrefix = "with")
public class GetRequestParam {

    public final GetRequestParam DEFAULT = GetRequestParam.builder()
            .build();

    @Nullable
    private final Map<String, String> query = new HashMap<String, String>(0);

}
