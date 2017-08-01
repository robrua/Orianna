package com.merakianalytics.orianna.type.data.champion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.merakianalytics.orianna.type.common.Platform;
import com.merakianalytics.orianna.type.common.Region;
import com.merakianalytics.orianna.type.data.CoreData;

public class ChampionStatus extends CoreData {
    private static final long serialVersionUID = 4229915263452433550L;
    private boolean enabledInRanked, enabledInCustoms, enabledInCoopVsAI, enabled, freeToPlay;
    private long id;
    private Platform platform;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * @return the region
     */
    @JsonIgnore
    public Region getRegion() {
        return platform == null ? null : platform.getRegion();
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @return the enabledInCoopVsAI
     */
    public boolean isEnabledInCoopVsAI() {
        return enabledInCoopVsAI;
    }

    /**
     * @return the enabledInCustoms
     */
    public boolean isEnabledInCustoms() {
        return enabledInCustoms;
    }

    /**
     * @return the enabledInRanked
     */
    public boolean isEnabledInRanked() {
        return enabledInRanked;
    }

    /**
     * @return the freeToPlay
     */
    public boolean isFreeToPlay() {
        return freeToPlay;
    }

    /**
     * @param enabled
     *        the enabled to set
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @param enabledInCoopVsAI
     *        the enabledInCoopVsAI to set
     */
    public void setEnabledInCoopVsAI(final boolean enabledInCoopVsAI) {
        this.enabledInCoopVsAI = enabledInCoopVsAI;
    }

    /**
     * @param enabledInCustoms
     *        the enabledInCustoms to set
     */
    public void setEnabledInCustoms(final boolean enabledInCustoms) {
        this.enabledInCustoms = enabledInCustoms;
    }

    /**
     * @param enabledInRanked
     *        the enabledInRanked to set
     */
    public void setEnabledInRanked(final boolean enabledInRanked) {
        this.enabledInRanked = enabledInRanked;
    }

    /**
     * @param freeToPlay
     *        the freeToPlay to set
     */
    public void setFreeToPlay(final boolean freeToPlay) {
        this.freeToPlay = freeToPlay;
    }

    /**
     * @param id
     *        the id to set
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @param platform
     *        the platform to set
     */
    public void setPlatform(final Platform platform) {
        this.platform = platform;
    }

    /**
     * @param region
     *        the region to set
     */
    public void setRegion(final Region region) {
        platform = region == null ? null : region.getPlatform();
    }
}
