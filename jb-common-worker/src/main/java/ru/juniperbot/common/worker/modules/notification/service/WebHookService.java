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
package ru.juniperbot.common.worker.modules.notification.service;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Webhook;

public interface WebHookService {

    Webhook getWebHook(long id, Guild guild);

    boolean updateWebHook(long id, long guildId, String channelId, String name, String iconUrl);

    boolean delete(long id, long guildId);

    void invalidateCache(long guildId);
}
