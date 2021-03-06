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
package ru.juniperbot.worker.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ru.juniperbot.common.worker.command.model.AbstractCommand;
import ru.juniperbot.common.worker.command.model.BotContext;
import ru.juniperbot.common.worker.command.model.DiscordCommand;
import ru.juniperbot.common.worker.modules.moderation.service.ModerationService;

@DiscordCommand(key = "discord.command.mod.slow.key",
        description = "discord.command.mod.slow.desc",
        group = "discord.command.group.moderation",
        permissions = {Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_MANAGE},
        priority = 40)
public class SlowModeCommand extends AbstractCommand {

    @Autowired
    protected ModerationService moderationService;

    @Override
    public boolean doCommand(GuildMessageReceivedEvent event, BotContext context, String query) {
        TextChannel channel = event.getChannel();
        if (!StringUtils.isNumeric(query)) {
            return showHelp(channel, context);
        }

        int seconds;
        try {
            seconds = Integer.parseInt(query);
        } catch (NumberFormatException e) {
            return showHelp(channel, context);
        }
        if (seconds < 0 || seconds > 120) {
            return showHelp(channel, context);
        }

        channel.sendTyping().queue();
        channel.getManager().setSlowmode(seconds).queue(e -> contextService.withContext(channel.getGuild(), () -> {
            if (seconds > 0) {
                String secPlurals = messageService.getCountPlural(seconds, "discord.plurals.second");
                messageService.onEmbedMessage(channel, "discord.command.mod.slow.enabled", seconds,
                        secPlurals);
            } else {
                messageService.onEmbedMessage(channel, "discord.command.mod.slow.disabled");
            }
        }));
        return true;
    }

    @Override
    public boolean isAvailable(User user, Member member, Guild guild) {
        return member != null && moderationService.isModerator(member);
    }

    private boolean showHelp(TextChannel channel, BotContext context) {
        String slowCommand = messageService.getMessageByLocale("discord.command.mod.slow.key",
                context.getCommandLocale());
        messageService.onEmbedMessage(channel, "discord.command.mod.slow.help",
                context.getConfig().getPrefix(), slowCommand);
        return false;
    }
}
