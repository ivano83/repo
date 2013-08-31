package it.fivano.symusic.backend.model;

import java.io.Serializable;

public class Release implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release.id_release
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private Long idRelease;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release.release_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String releaseName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release.author
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String author;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release.song_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String songName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release.years
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String years;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release.crew
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String crew;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table release
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release.id_release
     *
     * @return the value of release.id_release
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public Long getIdRelease() {
        return idRelease;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release.id_release
     *
     * @param idRelease the value for release.id_release
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setIdRelease(Long idRelease) {
        this.idRelease = idRelease;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release.release_name
     *
     * @return the value of release.release_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getReleaseName() {
        return releaseName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release.release_name
     *
     * @param releaseName the value for release.release_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release.author
     *
     * @return the value of release.author
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getAuthor() {
        return author;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release.author
     *
     * @param author the value for release.author
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release.song_name
     *
     * @return the value of release.song_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getSongName() {
        return songName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release.song_name
     *
     * @param songName the value for release.song_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setSongName(String songName) {
        this.songName = songName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release.years
     *
     * @return the value of release.years
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getYears() {
        return years;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release.years
     *
     * @param years the value for release.years
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setYears(String years) {
        this.years = years;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release.crew
     *
     * @return the value of release.crew
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getCrew() {
        return crew;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release.crew
     *
     * @param crew the value for release.crew
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setCrew(String crew) {
        this.crew = crew;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Release other = (Release) that;
        return (this.getIdRelease() == null ? other.getIdRelease() == null : this.getIdRelease().equals(other.getIdRelease()))
            && (this.getReleaseName() == null ? other.getReleaseName() == null : this.getReleaseName().equals(other.getReleaseName()))
            && (this.getAuthor() == null ? other.getAuthor() == null : this.getAuthor().equals(other.getAuthor()))
            && (this.getSongName() == null ? other.getSongName() == null : this.getSongName().equals(other.getSongName()))
            && (this.getYears() == null ? other.getYears() == null : this.getYears().equals(other.getYears()))
            && (this.getCrew() == null ? other.getCrew() == null : this.getCrew().equals(other.getCrew()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIdRelease() == null) ? 0 : getIdRelease().hashCode());
        result = prime * result + ((getReleaseName() == null) ? 0 : getReleaseName().hashCode());
        result = prime * result + ((getAuthor() == null) ? 0 : getAuthor().hashCode());
        result = prime * result + ((getSongName() == null) ? 0 : getSongName().hashCode());
        result = prime * result + ((getYears() == null) ? 0 : getYears().hashCode());
        result = prime * result + ((getCrew() == null) ? 0 : getCrew().hashCode());
        return result;
    }
}