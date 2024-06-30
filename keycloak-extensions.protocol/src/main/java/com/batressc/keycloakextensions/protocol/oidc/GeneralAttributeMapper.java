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

package com.batressc.keycloakextensions.protocol.oidc;

import com.batressc.keycloakextensions.protocol.oidc.models.Constants;
import com.batressc.keycloakextensions.protocol.oidc.models.MapperConfiguration;
import org.keycloak.models.AuthenticatedClientSessionModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperContainerModel;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleMapperModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.ProtocolMapperConfigException;
import org.keycloak.protocol.ProtocolMapperUtils;
import org.keycloak.protocol.oidc.mappers.AbstractOIDCProtocolMapper;
import org.keycloak.protocol.oidc.mappers.OIDCAccessTokenMapper;
import org.keycloak.protocol.oidc.mappers.OIDCAttributeMapperHelper;
import org.keycloak.protocol.oidc.mappers.OIDCIDTokenMapper;
import org.keycloak.protocol.oidc.mappers.TokenIntrospectionTokenMapper;
import org.keycloak.protocol.oidc.mappers.UserInfoTokenMapper;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.IDToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class GeneralAttributeMapper
    extends AbstractOIDCProtocolMapper
    implements OIDCAccessTokenMapper, OIDCIDTokenMapper, UserInfoTokenMapper, TokenIntrospectionTokenMapper {
    private static final String PROVIDER_ID = "oidc-general-attribute-mapper";
    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    static {
        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setName(Constants.ATTRIBUTE);
        property.setLabel(Constants.ATTRIBUTE_LABEL);
        property.setHelpText(Constants.ATTRIBUTE_HELP_TEXT);
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setRequired(true);
        configProperties.add(property);
        OIDCAttributeMapperHelper.addAttributeConfig(configProperties, GeneralAttributeMapper.class);

        property = new ProviderConfigProperty();
        property.setName(ProtocolMapperUtils.MULTIVALUED);
        property.setLabel(ProtocolMapperUtils.MULTIVALUED_LABEL);
        property.setHelpText(ProtocolMapperUtils.MULTIVALUED_HELP_TEXT);
        property.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        configProperties.add(property);

        property = new ProviderConfigProperty();
        property.setName(ProtocolMapperUtils.AGGREGATE_ATTRS);
        property.setLabel(ProtocolMapperUtils.AGGREGATE_ATTRS_LABEL);
        property.setHelpText(ProtocolMapperUtils.AGGREGATE_ATTRS_HELP_TEXT);
        property.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        configProperties.add(property);

        property = new ProviderConfigProperty();
        property.setName(Constants.INCLUDE_ATTRS_USER);
        property.setLabel(Constants.INCLUDE_ATTRS_USER_LABEL);
        property.setHelpText(Constants.INCLUDE_ATTRS_USER_HELP_TEXT);
        property.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        configProperties.add(property);

        property = new ProviderConfigProperty();
        property.setName(Constants.INCLUDE_ATTRS_ROLE_REALM);
        property.setLabel(Constants.INCLUDE_ATTRS_ROLE_REALM_LABEL);
        property.setHelpText(Constants.INCLUDE_ATTRS_ROLE_REALM_HELP_TEXT);
        property.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        configProperties.add(property);

        property = new ProviderConfigProperty();
        property.setName(Constants.INCLUDE_ATTRS_ROLE_CLIENT);
        property.setLabel(Constants.INCLUDE_ATTRS_ROLE_CLIENT_LABEL);
        property.setHelpText(Constants.INCLUDE_ATTRS_ROLE_CLIENT_HELP_TEXT);
        property.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        configProperties.add(property);

        property = new ProviderConfigProperty();
        property.setName(Constants.INCLUDE_ATTRS_GROUP);
        property.setLabel(Constants.INCLUDE_ATTRS_GROUP_LABEL);
        property.setHelpText(Constants.INCLUDE_ATTRS_GROUP_HELP_TEXT);
        property.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        configProperties.add(property);

        property = new ProviderConfigProperty();
        property.setName(Constants.INCLUDE_ATTRS_GROUP_ROLES);
        property.setLabel(Constants.INCLUDE_ATTRS_GROUP_ROLES_LABEL);
        property.setHelpText(Constants.INCLUDE_ATTRS_GROUP_ROLES_HELP_TEXT);
        property.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        configProperties.add(property);
    }


    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return Constants.DISPLAY_TYPE;
    }

    @Override
    public String getDisplayCategory() {
        return TOKEN_MAPPER_CATEGORY;
    }

    @Override
    public String getHelpText() {
        return Constants.HELP_TEXT;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    private MapperConfiguration getConfiguration(ProtocolMapperModel mapperModel) {
        Map<String, String> mapperConfiguration = mapperModel.getConfig();
        return new MapperConfiguration(
            mapperConfiguration.getOrDefault(Constants.ATTRIBUTE, null),
            mapperConfiguration.getOrDefault(ProtocolMapperUtils.AGGREGATE_ATTRS, "false").equals("true"),
            mapperConfiguration.getOrDefault(Constants.INCLUDE_ATTRS_USER, "false").equals("true"),
            mapperConfiguration.getOrDefault(Constants.INCLUDE_ATTRS_ROLE_REALM, "false").equals("true"),
            mapperConfiguration.getOrDefault(Constants.INCLUDE_ATTRS_ROLE_CLIENT, "false").equals("true"),
            mapperConfiguration.getOrDefault(Constants.INCLUDE_ATTRS_GROUP, "false").equals("true"),
            mapperConfiguration.getOrDefault(Constants.INCLUDE_ATTRS_GROUP_ROLES, "false").equals("true")
        );
    }

    @Override
    public void validateConfig(KeycloakSession session, RealmModel realm, ProtocolMapperContainerModel client, ProtocolMapperModel mapperModel) throws ProtocolMapperConfigException {
        MapperConfiguration mapperConfiguration = getConfiguration(mapperModel);
        boolean includeConfigured =
            mapperConfiguration.includeUserAttrs() || mapperConfiguration.includeRealmRoleAttrs() ||
            mapperConfiguration.includeClientRoleAttrs() || mapperConfiguration.includeGroupAttrs();
        if (!includeConfigured) {
            throw new ProtocolMapperConfigException(Constants.ERROR_INCLUDE_SELECTION, "error");
        }
        boolean groupConfigured =
            mapperConfiguration.includeGroupAttrs() && mapperConfiguration.includeGroupRolesAttrs() &&
            (mapperConfiguration.includeRealmRoleAttrs() || mapperConfiguration.includeClientRoleAttrs());
        if (mapperConfiguration.includeGroupAttrs() && mapperConfiguration.includeGroupRolesAttrs() && !groupConfigured) {
            throw new ProtocolMapperConfigException(Constants.ERROR_GROUP_SELECTION, "error");
        }
    }

    private List<String> getAttributeValuesFromRole(RoleModel role, String attributeName) {
        List<String> attributeValues = new ArrayList<>(role.getAttributeStream(attributeName).toList());
        List<RoleModel> compositeRoles = role.getCompositesStream().toList();
        for (RoleModel compositeRole : compositeRoles) {
            attributeValues.addAll(getAttributeValuesFromRole(compositeRole, attributeName));
        }
        return attributeValues;
    }

    private List<String> getAttributeValuesFromRoleTypes(
            RoleMapperModel element, String attributeName,
            boolean includeRoleRealmAttrs, boolean includeClientRoleAttrs, ClientSessionContext clientSessionCtx
    ) {
        List<String> attributeValues = new ArrayList<>();
        if (includeRoleRealmAttrs) {
            List<RoleModel> realmRoles = element.getRealmRoleMappingsStream().toList();
            for (RoleModel realmRole : realmRoles) {
                attributeValues.addAll(getAttributeValuesFromRole(realmRole, attributeName));
            }
        }
        if (includeClientRoleAttrs && clientSessionCtx != null) {
            AuthenticatedClientSessionModel clientSession = clientSessionCtx.getClientSession();
            if (clientSession != null) {
                ClientModel clientModel = clientSession.getClient();
                if (clientModel != null) {
                    List<RoleModel> clientRoles = element.getClientRoleMappingsStream(clientModel).toList();
                    for (RoleModel clientRole : clientRoles) {
                        attributeValues.addAll(getAttributeValuesFromRole(clientRole, attributeName));
                    }
                }
            }
        }
        return attributeValues;
    }

    private List<String> getAttributeValuesFromGroup(
            GroupModel group, String attributeName,
            boolean includeGroupRolesAttrs, boolean includeRoleRealmAttrs, boolean includeClientRoleAttrs, ClientSessionContext clientSessionCtx
    ) {
        List<String> attributeValues = new ArrayList<>(group.getAttributeStream(attributeName).toList());
        if (includeGroupRolesAttrs) {
            attributeValues.addAll(
                    getAttributeValuesFromRoleTypes(group, attributeName, includeRoleRealmAttrs, includeClientRoleAttrs, clientSessionCtx)
            );
        }
        List<GroupModel> childrenGroups = group.getSubGroupsStream().toList();
        for (GroupModel childGroup : childrenGroups) {
            attributeValues.addAll(getAttributeValuesFromGroup(
                    childGroup, attributeName,
                    includeGroupRolesAttrs, includeRoleRealmAttrs, includeClientRoleAttrs, clientSessionCtx
            ));
        }
        return attributeValues;
    }

    private Collection<String> resolveAttribute(
            UserModel user, ClientSessionContext clientSessionCtx, String attributeName, boolean aggregateAttrs,
            boolean includeUserAttrs, boolean includeRoleRealmAttrs, boolean includeClientRoleAttrs, boolean includeGroupAttrs, boolean includeGroupRoleAttrs
    ) {
        // Attribute values from users
        List<String> attributeValues = new ArrayList<>();
        if (includeUserAttrs) {
            attributeValues.addAll(user.getAttributeStream(attributeName).toList());
        }
        // Attribute values from realm roles & client roles
        attributeValues.addAll(getAttributeValuesFromRoleTypes(user, attributeName, includeRoleRealmAttrs, includeClientRoleAttrs, clientSessionCtx));
        // Attribute values from groups
        if (includeGroupAttrs) {
            List<GroupModel> groups = user.getGroupsStream().toList();
            for (GroupModel group : groups) {
                attributeValues.addAll(getAttributeValuesFromGroup(
                        group, attributeName,
                        includeGroupRoleAttrs, includeRoleRealmAttrs, includeClientRoleAttrs, clientSessionCtx
                ));
            }
        }
        if (aggregateAttrs) {
            return new HashSet<>(attributeValues);
        }
        return attributeValues;
    }

    @Override
    protected void setClaim(IDToken token, ProtocolMapperModel mappingModel, UserSessionModel userSession, KeycloakSession keycloakSession, ClientSessionContext clientSessionCtx) {
        UserModel user = userSession.getUser();
        MapperConfiguration mapperConfiguration = getConfiguration(mappingModel);

        if (mapperConfiguration.attributeName() == null) {
            return;
        }
        Collection<String> attributeValues = resolveAttribute(
            user, clientSessionCtx,
            mapperConfiguration.attributeName(),
            mapperConfiguration.aggregateAttributes(),
            mapperConfiguration.includeUserAttrs(),
            mapperConfiguration.includeRealmRoleAttrs(),
            mapperConfiguration.includeClientRoleAttrs(),
            mapperConfiguration.includeGroupAttrs(),
            mapperConfiguration.includeGroupRolesAttrs()
        );
        OIDCAttributeMapperHelper.mapClaim(token, mappingModel, attributeValues);
    }
}
