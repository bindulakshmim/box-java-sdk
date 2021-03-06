package com.box.sdk;

import java.text.ParseException;
import java.util.Date;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * Represents a link to a file or folder on Box.
 */
public class BoxSharedLink extends BoxJSONObject {
    private String url;
    private String downloadUrl;
    private String vanityUrl;
    private boolean isPasswordEnabled;
    private Date unsharedAt;
    private long downloadCount;
    private long previewCount;
    private Access access;
    private Permissions permissions;

    /**
     * Constructs a BoxSharedLink with default settings.
     */
    public BoxSharedLink() { }

    /**
     * Constructs a BoxSharedLink from a JSON string.
     * @param  json the JSON encoded shared link.
     */
    public BoxSharedLink(String json) {
        super(json);
    }

    BoxSharedLink(JsonObject jsonObject) {
        super(jsonObject);
    }

    BoxSharedLink(BoxSharedLink.Access access, Date unshareDate, BoxSharedLink.Permissions permissions) {
        this.setAccess(access);
        this.setPermissions(permissions);

        if (unshareDate != null) {
            this.setUnsharedDate(unshareDate);
        }
    }

    /**
     * Get the URL of this shared link.
     * @return the URL of this shared link.
     */
    public String getURL() {
        return this.url;
    }

    /**
     * Gets the direct download URL of this shared link.
     * @return the direct download URL of this shared link.
     */
    public String getDownloadURL() {
        return this.downloadUrl;
    }

    /**
     * Gets the vanity URL of this shared link.
     * @return the vanity URL of this shared link.
     */
    public String getVanityURL() {
        return this.vanityUrl;
    }

    /**
     * Gets whether or not a password is enabled on this shared link.
     * @return true if there's a password enabled on this shared link; otherwise false.
     */
    public boolean getIsPasswordEnabled() {
        return this.isPasswordEnabled;
    }

    /**
     * Gets the time that this shared link will be deactivated.
     * @return the time that this shared link will be deactivated.
     */
    public Date getUnsharedDate() {
        return this.unsharedAt;
    }

    /**
     * Sets the time that this shared link will be deactivated.
     * @param unsharedDate the time that this shared link will be deactivated.
     */
    public void setUnsharedDate(Date unsharedDate) {
        this.unsharedAt = unsharedDate;
        this.addPendingChange("unshared_at", unsharedDate.toString());
    }

    /**
     * Gets the number of times that this shared link has been downloaded.
     * @return the number of times that this link has been downloaded.
     */
    public long getDownloadCount() {
        return this.downloadCount;
    }

    /**
     * Gets the number of times that this shared link has been previewed.
     * @return the number of times that this link has been previewed.
     */
    public long getPreviewCount() {
        return this.previewCount;
    }

    /**
     * Gets the access level of this shared link.
     * @return the access level of this shared link.
     */
    public Access getAccess() {
        return this.access;
    }

    /**
     * Sets the access level of this shared link.
     * @param access the new acccess level of this shared link.
     */
    public void setAccess(Access access) {
        this.access = access;
        this.addPendingChange("access", access.toJSONValue());
    }

    /**
     * Gets the permissions associated with this shared link.
     * @return the permissions associated with this shared link.
     */
    public Permissions getPermissions() {
        return this.permissions;
    }

    /**
     * Sets the permissions associated with this shared link.
     * @param permissions the new permissions for this shared link.
     */
    public void setPermissions(Permissions permissions) {
        if (this.permissions == permissions) {
            return;
        }

        this.removeChildObject("permissions");
        this.permissions = permissions;
        this.addChildObject("permissions", permissions);
    }

    @Override
    void parseJSONMember(JsonObject.Member member) {
        JsonValue value = member.getValue();
        try {
            switch (member.getName()) {
                case "url":
                    this.url = value.asString();
                    break;
                case "download_url":
                    this.downloadUrl = value.asString();
                    break;
                case "vanity_url":
                    this.vanityUrl = value.asString();
                    break;
                case "is_password_enabled":
                    this.isPasswordEnabled = value.asBoolean();
                    break;
                case "unshared_at":
                    this.unsharedAt = BoxDateFormat.parse(value.asString());
                    break;
                case "download_count":
                    this.downloadCount = Double.valueOf(value.toString()).longValue();
                    break;
                case "preview_count":
                    this.previewCount = Double.valueOf(value.toString()).longValue();
                    break;
                case "access":
                    String accessString = value.asString().toUpperCase();
                    this.access = Access.valueOf(accessString);
                    break;
                case "permissions":
                    if (this.permissions == null) {
                        this.setPermissions(new Permissions(value.asObject()));
                    } else {
                        this.permissions.update(value.asObject());
                    }
                    break;
                default:
                    break;
            }
        } catch (ParseException e) {
            assert false : "A ParseException indicates a bug in the SDK.";
        }
    }

    /**
     * Contains permissions fields that can be set on a shared link.
     */
    public static class Permissions extends BoxJSONObject {
        private boolean canDownload;
        private boolean canPreview;

        /**
         * Constructs a Permissions object with all permissions disabled.
         */
        public Permissions() { }

        /**
         * Constructs a Permissions object from a JSON string.
         * @param  json the JSON encoded shared link permissions.
         */
        public Permissions(String json) {
            super(json);
        }

        Permissions(JsonObject jsonObject) {
            super(jsonObject);
        }

        /**
         * Gets whether or not the shared link can be downloaded.
         * @return true if the shared link can be downloaded; otherwise false.
         */
        public boolean getCanDownload() {
            return this.canDownload;
        }

        /**
         * Sets whether or not the shared link can be downloaded.
         * @param enabled true if the shared link can be downloaded; otherwise false.
         */
        public void setCanDownload(boolean enabled) {
            this.canDownload = enabled;
            this.addPendingChange("can_download", enabled);
        }

        /**
         * Gets whether or not the shared link can be previewed.
         * @return true if the shared link can be previewed; otherwise false.
         */
        public boolean getCanPreview() {
            return this.canPreview;
        }

        /**
         * Sets whether or not the shared link can be previewed.
         * @param enabled true if the shared link can be previewed; otherwise false.
         */
        public void setCanPreview(boolean enabled) {
            this.canPreview = enabled;
            this.addPendingChange("can_preview", enabled);
        }

        @Override
        void parseJSONMember(JsonObject.Member member) {
            JsonValue value = member.getValue();
            switch (member.getName()) {
                case "can_download":
                    this.canDownload = value.asBoolean();
                    break;
                case "can_preview":
                    this.canPreview = value.asBoolean();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Enumerates the possible access levels that can be set on a shared link.
     */
    public enum Access {
        /**
         * The default access level for the user or enterprise.
         */
        DEFAULT (null),

        /**
         * The link can be accessed by anyone.
         */
        OPEN ("open"),

        /**
         * The link can be accessed by other users within the company.
         */
        COMPANY ("company"),

        /**
         * The link can be accessed by other collaborators.
         */
        COLLABORATORS ("collaborators");

        private final String jsonValue;

        private Access(String jsonValue) {
            this.jsonValue = jsonValue;
        }

        String toJSONValue() {
            return this.jsonValue;
        }
    }
}
