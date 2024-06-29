/*
 * Copyright 2024 Luis Gustavo Fernández Batres
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.batressc.keycloakextensions.protocol.models;

public final class Constants {
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public final static String DISPLAY_TYPE = "Attribute from Users, Realm Roles, Client Roles or Groups";
    public final static String HELP_TEXT = "Map an attribute from a User, Realm Role, Client Role or Group to a token claim.";

    public final static String ATTRIBUTE = "oidc.general.attribute.name";
    public final static String ATTRIBUTE_LABEL = "Attribute";
    public final static String ATTRIBUTE_HELP_TEXT = "Name of the attribute to map to the token claim.";

    public final static String INCLUDE_ATTRS_USER = "oidc.general.include.attributes.user";
    public final static String INCLUDE_ATTRS_USER_LABEL = "Include user attributes";
    public final static String INCLUDE_ATTRS_USER_HELP_TEXT = "Include attributes defined on the User.";

    public final static String INCLUDE_ATTRS_ROLE_REALM = "oidc.general.include.attributes.role.realm";
    public final static String INCLUDE_ATTRS_ROLE_REALM_LABEL = "Include realm role attributes";
    public final static String INCLUDE_ATTRS_ROLE_REALM_HELP_TEXT = "Include attributes defined on the realm roles.";

    public final static String INCLUDE_ATTRS_ROLE_CLIENT = "oidc.general.include.attributes.role.client";
    public final static String INCLUDE_ATTRS_ROLE_CLIENT_LABEL = "Include client role attributes";
    public final static String INCLUDE_ATTRS_ROLE_CLIENT_HELP_TEXT = "Include attributes defined on the client roles.";

    public final static String INCLUDE_ATTRS_GROUP = "oidc.general.include.attributes.group";
    public final static String INCLUDE_ATTRS_GROUP_LABEL = "Include group attributes";
    public final static String INCLUDE_ATTRS_GROUP_HELP_TEXT = "Include attributes defined on the groups.";

    public final static String INCLUDE_ATTRS_GROUP_ROLES = "oidc.general.include.attributes.group.role";
    public final static String INCLUDE_ATTRS_GROUP_ROLES_LABEL = "Include roles associated in the group";
    public final static String INCLUDE_ATTRS_GROUP_ROLES_HELP_TEXT = "Include attributes defined on the roles associated in the group. The roles to include depends of the role options selected.";

    public final static String ERROR_INCLUDE_SELECTION = "At least one type of attribute must be selected to include.";
    public final static String ERROR_GROUP_SELECTION = "You must select at least one type of role when choosing the option to include associated roles in groups.";
}
