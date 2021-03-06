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
package ru.juniperbot.api.dto.config;

import lombok.Getter;
import lombok.Setter;
import ru.juniperbot.api.dto.MessageTemplateDto;
import ru.juniperbot.common.model.RankingReward;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class RankingDto implements Serializable {
    private static final long serialVersionUID = 3035748702987797559L;

    private boolean enabled;

    private boolean voiceEnabled;

    private boolean announcementEnabled;

    @Valid
    private MessageTemplateDto announceTemplate;

    private boolean resetOnLeave;

    private String[] bannedRoles;

    private List<RankingReward> rewards;

    private Set<String> ignoredChannels;

    private Set<String> ignoredVoiceChannels;

    @Min(50)
    @Max(500)
    private int textExpMultiplier = 100;

    @Min(50)
    @Max(500)
    private int voiceExpMultiplier = 100;

    @Min(2)
    private Integer maxVoiceMembers;

    private boolean cookieEnabled;
}
