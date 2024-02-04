@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownField = false)
public record ApplicationConfig(@NotNull String test) {}