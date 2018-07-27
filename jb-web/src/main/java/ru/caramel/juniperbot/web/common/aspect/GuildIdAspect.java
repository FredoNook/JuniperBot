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
package ru.caramel.juniperbot.web.common.aspect;

import java.lang.annotation.Annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.caramel.juniperbot.core.model.exception.AccessDeniedException;
import ru.caramel.juniperbot.core.model.exception.NotFoundException;
import ru.caramel.juniperbot.web.security.auth.DiscordTokenServices;
import ru.caramel.juniperbot.web.security.model.DiscordGuildDetails;

@Component
@Aspect
public class GuildIdAspect {

    @Autowired
    protected DiscordTokenServices tokenServices;

    @Around("execution(public * *(.., @ru.caramel.juniperbot.web.common.aspect.GuildId (*), ..))")
    public Object doAwesomeStuff(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        Object[] methodArgs = thisJoinPoint.getArgs();
        int numArgs = methodArgs.length;
        MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
        Annotation[][] annotationMatrix = methodSignature.getMethod().getParameterAnnotations();
        for (int i = 0; i < numArgs; i++) {
            Annotation[] annotations = annotationMatrix[i];
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == GuildId.class && methodArgs[i] instanceof Long) {
                    DiscordGuildDetails details = tokenServices.getGuildById((long) methodArgs[i]);
                    if (details == null) {
                        throw new NotFoundException();
                    }
                    if (!tokenServices.hasPermission(details)) {
                        throw new AccessDeniedException();
                    }
                }
            }
        }
        return thisJoinPoint.proceed();
    }
}