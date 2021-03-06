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
package ru.juniperbot.api.controller.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.juniperbot.api.ApiProperties;
import ru.juniperbot.api.service.ApiMapperService;
import ru.juniperbot.common.service.ConfigService;
import ru.juniperbot.common.service.GatewayService;

@RequestMapping("api/priv")
public abstract class BaseRestController {

    @Autowired
    protected ApiProperties apiProperties;

    @Autowired
    protected ConfigService configService;

    @Autowired
    protected ApiMapperService apiMapperService;

    @Autowired
    protected TaskExecutor taskExecutor;

    @Autowired
    protected GatewayService gatewayService;
}
