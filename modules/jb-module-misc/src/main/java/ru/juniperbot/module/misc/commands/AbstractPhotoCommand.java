/*
 * This file is part of JuniperBot.
 *
 * JuniperBot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * JuniperBot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JuniperBot. If not, see <http://www.gnu.org/licenses/>.
 */
package ru.juniperbot.module.misc.commands;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.juniperbot.common.utils.CommonUtils;
import ru.juniperbot.common.worker.command.model.AbstractCommandAsync;
import ru.juniperbot.common.worker.command.model.BotContext;
import ru.juniperbot.module.misc.model.RandomPhotoResponse;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractPhotoCommand extends AbstractCommandAsync {

    private RestTemplate restTemplate;

    private final Supplier<RandomPhotoResponse> photoResult =
            Suppliers.memoizeWithExpiration(this::getPhoto, 1, TimeUnit.SECONDS);

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate(CommonUtils.createRequestFactory());
    }

    private RandomPhotoResponse getPhoto() {
        ResponseEntity<RandomPhotoResponse> response = restTemplate.getForEntity(getEndPoint(), RandomPhotoResponse.class);
        if (response.getStatusCode() != HttpStatus.OK
                || response.getBody() == null
                || StringUtils.isEmpty(response.getBody().getFile())) {
            return null;
        }
        return response.getBody();
    }

    @Override
    public void doCommandAsync(GuildMessageReceivedEvent message, BotContext context, String query) {
        try {
            message.getChannel().sendTyping().queue();
            RandomPhotoResponse response = photoResult.get();
            if (response == null) {
                messageService.onEmbedMessage(message.getChannel(), getErrorCode());
                return;
            }
            EmbedBuilder builder = messageService.getBaseEmbed();
            builder.setImage(response.getFile());
            builder.setColor(null);
            messageService.sendMessageSilent(message.getChannel()::sendMessage, builder.build());
        } catch (Exception e) {
            log.warn("Could not get photo");
            messageService.onEmbedMessage(message.getChannel(), getErrorCode());
        }
    }

    protected abstract String getEndPoint();

    protected abstract String getErrorCode();

}
