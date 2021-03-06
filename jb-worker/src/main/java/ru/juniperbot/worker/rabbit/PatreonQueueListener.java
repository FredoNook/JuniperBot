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
package ru.juniperbot.worker.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.juniperbot.common.configuration.RabbitConfiguration;
import ru.juniperbot.common.model.request.PatreonRequest;
import ru.juniperbot.common.worker.patreon.service.PatreonService;

@EnableRabbit
@Component
@Slf4j
public class PatreonQueueListener extends BaseQueueListener {

    @Autowired
    private PatreonService patreonService;

    @RabbitListener(queues = RabbitConfiguration.QUEUE_PATREON_WEBHOOK_REQUEST)
    public boolean updatePatreon(PatreonRequest request) {
        return patreonService.processWebHook(request);
    }
}
