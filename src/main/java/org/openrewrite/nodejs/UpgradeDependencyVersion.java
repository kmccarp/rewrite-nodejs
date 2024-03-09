/*
 * Copyright 2024 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.nodejs;

import org.openrewrite.*;
import org.openrewrite.json.JsonPathMatcher;
import org.openrewrite.json.JsonVisitor;
import org.openrewrite.json.tree.Json;
import org.openrewrite.nodejs.search.IsPackageJson;

public class UpgradeDependencyVersion extends Recipe {

    @Option(displayName = "Update `package-lock.json`",
            description = "As of NPM 5.1.",
            example = "react-dom")

    @Override
    public String getDisplayName() {
        return "Upgrade Node.js dependencies";
    }

    @Override
    public String getDescription() {
        return "Upgrade matching Node.js dependencies";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        // NOTE that `npm upgrade <pkg>` will update the package-lock.json as well.
        // we might consider modifying the package.json with the new version and then
        // triggering that.
        JsonPathMatcher dependency = new JsonPathMatcher("$.dependencies");
        return Preconditions.check(new IsPackageJson<>(), new JsonVisitor<ExecutionContext>() {
            @Override
            public Json visitMember(Json.Member member, ExecutionContext ctx) {
                if (dependency.matches(getCursor())) {
                    System.out.println("here");
                }
                return super.visitMember(member, ctx);
            }
        });
    }
}
