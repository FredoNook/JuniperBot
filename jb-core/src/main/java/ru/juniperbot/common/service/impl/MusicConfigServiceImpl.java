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
package ru.juniperbot.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.juniperbot.common.persistence.entity.MusicConfig;
import ru.juniperbot.common.persistence.repository.MusicConfigRepository;
import ru.juniperbot.common.service.MusicConfigService;
import ru.juniperbot.common.service.impl.AbstractDomainServiceImpl;

@Service
public class MusicConfigServiceImpl extends AbstractDomainServiceImpl<MusicConfig, MusicConfigRepository> implements MusicConfigService {

    public MusicConfigServiceImpl(@Autowired MusicConfigRepository repository) {
        super(repository, true);
    }

    @Override
    protected MusicConfig createNew(long guildId) {
        MusicConfig config = new MusicConfig(guildId);
        config.setVoiceVolume(100);
        return config;
    }

    @Override
    @Transactional
    public void updateVolume(long guildId, int volume) {
        repository.updateVolume(guildId, volume);
        cacheManager.evict(MusicConfig.class, guildId);
    }

    @Override
    protected Class<MusicConfig> getDomainClass() {
        return MusicConfig.class;
    }
}