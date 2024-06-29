# OIDC General Attribute Mapper

This custom mapper is based on [`UserAttributeMapper.java`](https://github.com/keycloak/keycloak/blob/main/services/src/main/java/org/keycloak/protocol/oidc/mappers/UserAttributeMapper.java), which allows mapping attributes to token claims. The attributes can be obtained from one or more of the following elements:
- Users
- Realm Roles
- Client Roles
- Groups
- Roles associated with Groups


## How to install

1. You must compile and generate the jar file of the `keycloak-extensions.protocol` module.
2. Put the generated jar file in your Keycloak provider folder and restart the server.

You can see a more detailed instructions on the [official Keycloak documentation](https://www.keycloak.org/server/configuration-provider)


## How to use

### Step 1: Create a client scope

1. Login to the Keycloak Administration Console.
2. Navigate to the `Client Scopes` section.
3. Click on `Create client scope`.
4. Enter the name, description, and the other information according your requirements. The `Protocol` field must be **OpenID Connect**.
5. Click on `Save`.

### Step 2: Add the OIDC General Attribute Mapper

1. After creating the client scope, click on the `Mappers` tab.
2. Click on `Add mapper by configuration`.
3. Click on the mapper with the name `Attribute from Users, Realm Roles, client roles or Groups`.
4. Enter the basic configuration: `Name`, `Attribute`, `Token Claim Name` and the others fields.
5. You must select at least one of the following options: `Include user attributes`, `Include realm role attributes`, `Include client role attributes`, `Include group attributes`.

   The option `Include roles associated in the group` works only when selecting the option `Include group attributes`. It is important to note that since groups can have associated realm roles as well as client roles, when including the roles of the groups, it is necessary to additionally specify at least one of these options: `Include realm role attributes` and `Include client role attributes`.

6. Click on `Save`.

### Step 3: Configure the Client to Use the Client Scope

1. Navigate to the `Clients` section.
2. Select the client you want to configure.
3. Go to the `Client Scopes` tab and select the `Setup` sub tab.
4. Click on the `Add client scope` and select the previous client scope configured.

### Step 4: Test the Configuration

1. Navigate to the `Clients` section.
2. Select the client previously configured.
3. Go to the `Client Scopes` tab and select the `Evaluate` sub tab.
4. Choose a user and click on `Generated access token`.
5. You should see the claim configured according the OIDC General Attribute Mapper.