package hu.martin.felookchatservice.config;

import hu.martin.felookchatservice.auth.ApplicationUserReadConverter;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {

    private final ConnectionFactory connectionFactory;

    public R2dbcConfiguration(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    @Override
    @Nonnull
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        converterList.add(new ApplicationUserReadConverter());
        return new R2dbcCustomConversions(getStoreConversions(), converterList);
    }

    @Override
    @Nonnull
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }
}
