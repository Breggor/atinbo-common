/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2005-2019 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.atinbo.common.reflections;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@link ResourceFinder} class provides fluent api to search for resources.
 */
public final class ResourceFinder {

    private ClassLoader loader;
    private Set<Pattern> namePatterns = new LinkedHashSet<>();
    private Set<Pattern> pathPatterns = new LinkedHashSet<>();

    ResourceFinder() {
    }

    ResourceFinder(ClassLoader loader) {
        this.loader = loader;
    }

    private static List<URL> getResources(Pattern pattern, ClassLoader loader, boolean partial) {
        final List<URL> all = new ArrayList<>();
        final ClassLoader classLoader =
                loader == null ? Thread.currentThread().getContextClassLoader() : loader;
        try {
            for (ResourceInfo info : ClassPath.from(classLoader).getResources()) {
                String name = info.getResourceName();
                Matcher matcher = pattern.matcher(name);
                boolean matched = partial ? matcher.find() : matcher.matches();
                if (matched) {
                    Enumeration<URL> urls = classLoader.getResources(name);
                    while (urls.hasMoreElements()) {
                        all.add(urls.nextElement());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Collections.unmodifiableList(all);
    }

    /**
     * Find with the given name pattern.
     *
     * @param pattern the name pattern
     * @return the same finder
     */
    public ResourceFinder byName(String pattern) {
        Objects.requireNonNull(pattern, "pattern must not be null");
        namePatterns.add(Pattern.compile(pattern));
        return this;
    }

    /**
     * Find with the given URL pattern.
     *
     * @param pattern the URL pattern
     * @return the same finder
     */
    public ResourceFinder byURL(String pattern) {
        Objects.requireNonNull(pattern, "pattern must not be null");
        pathPatterns.add(Pattern.compile(pattern));
        return this;
    }

    /**
     * Search the resources by full pattern match using {@link Matcher#matches()} call.
     *
     * @return list of URL objects
     */
    public List<URL> match() {
        return find(false);
    }

    /**
     * Search the resources by partial match using {@link Matcher#find()} call.
     *
     * @return list of URL objects
     */
    public List<URL> find() {
        return find(true);
    }

    private List<URL> find(boolean partial) {
        List<URL> all = new ArrayList<>();
        for (Pattern namePattern : namePatterns) {
            for (URL file : getResources(namePattern, loader, partial)) {
                if (pathPatterns.isEmpty()) {
                    all.add(file);
                    continue;
                }
                for (Pattern pathPattern : pathPatterns) {
                    Matcher matcher = pathPattern.matcher(file.getFile());
                    boolean matched = partial ? matcher.find() : matcher.matches();
                    if (matched) {
                        all.add(file);
                    }
                }
            }
        }
        return Collections.unmodifiableList(all);
    }
}
