/*
 * This file is part of JuniperBotJ.
 *
 * JuniperBotJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * JuniperBotJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JuniperBotJ. If not, see <http://www.gnu.org/licenses/>.
 */
package ru.caramel.juniperbot.core.audit.provider;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import ru.caramel.juniperbot.core.audit.model.AuditActionType;
import ru.caramel.juniperbot.core.audit.model.ForwardProvider;
import ru.caramel.juniperbot.core.audit.persistence.AuditAction;

@ForwardProvider(AuditActionType.VOICE_JOIN)
public class VoiceJoinAuditForwardProvider extends VoiceAuditForwardProvider {

    @Override
    protected void build(AuditAction action, MessageBuilder messageBuilder, EmbedBuilder embedBuilder) {
        if (action.getChannel() != null && action.getUser() != null) {
            embedBuilder.setDescription(getUserMessage(action, "audit.message.voice.join.message"));
            embedBuilder.setFooter(messageService.getMessage("audit.member.id", action.getUser().getId()), null);
        }
    }
}
