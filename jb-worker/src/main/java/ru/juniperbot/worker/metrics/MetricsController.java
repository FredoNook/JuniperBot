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
package ru.juniperbot.worker.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
public class MetricsController implements ErrorController {

    private final CollectorRegistry registry;

    public MetricsController() {
        this.registry = CollectorRegistry.defaultRegistry;
    }

    @GetMapping
    @ResponseBody
    public String index() {
        return "";
    }

    @GetMapping("error")
    @ResponseBody
    public String error() {
        return "Whoops, something went wrong";
    }

    @GetMapping("health")
    @ResponseBody
    public String health() {
        return "OK";
    }

    @GetMapping(value = "metrics", produces = TextFormat.CONTENT_TYPE_004)
    public ResponseEntity<String> metrics(@RequestParam(name = "name[]", required = false) String[] includedParam)
            throws IOException {
        Set<String> params = includedParam == null ? Collections.emptySet() : new HashSet<>(Arrays.asList(includedParam));
        try (Writer writer = new StringWriter()) {
            TextFormat.write004(writer, this.registry.filteredMetricFamilySamples(params));
            writer.flush();
            return new ResponseEntity<>(writer.toString(), HttpStatus.OK);
        }
    }

    @Override
    public String getErrorPath() {
        return "error";
    }
}
