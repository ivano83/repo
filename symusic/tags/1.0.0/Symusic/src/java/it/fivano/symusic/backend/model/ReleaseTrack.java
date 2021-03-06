package it.fivano.symusic.backend.model;

import java.io.Serializable;

public class ReleaseTrack implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release_track.id_release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private Long idReleaseTrack;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release_track.id_release
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private Long idRelease;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release_track.id_genre
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private Integer idGenre;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release_track.track_number
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String trackNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release_track.track_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String trackName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release_track.track_time
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String trackTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release_track.track_bpm
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String trackBpm;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release_track.track_genere
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String trackGenere;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column release_track.tag
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String tag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release_track.id_release_track
     *
     * @return the value of release_track.id_release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public Long getIdReleaseTrack() {
        return idReleaseTrack;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release_track.id_release_track
     *
     * @param idReleaseTrack the value for release_track.id_release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setIdReleaseTrack(Long idReleaseTrack) {
        this.idReleaseTrack = idReleaseTrack;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release_track.id_release
     *
     * @return the value of release_track.id_release
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public Long getIdRelease() {
        return idRelease;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release_track.id_release
     *
     * @param idRelease the value for release_track.id_release
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setIdRelease(Long idRelease) {
        this.idRelease = idRelease;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release_track.id_genre
     *
     * @return the value of release_track.id_genre
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public Integer getIdGenre() {
        return idGenre;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release_track.id_genre
     *
     * @param idGenre the value for release_track.id_genre
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setIdGenre(Integer idGenre) {
        this.idGenre = idGenre;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release_track.track_number
     *
     * @return the value of release_track.track_number
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getTrackNumber() {
        return trackNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release_track.track_number
     *
     * @param trackNumber the value for release_track.track_number
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release_track.track_name
     *
     * @return the value of release_track.track_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getTrackName() {
        return trackName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release_track.track_name
     *
     * @param trackName the value for release_track.track_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release_track.track_time
     *
     * @return the value of release_track.track_time
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getTrackTime() {
        return trackTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release_track.track_time
     *
     * @param trackTime the value for release_track.track_time
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setTrackTime(String trackTime) {
        this.trackTime = trackTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release_track.track_bpm
     *
     * @return the value of release_track.track_bpm
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getTrackBpm() {
        return trackBpm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release_track.track_bpm
     *
     * @param trackBpm the value for release_track.track_bpm
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setTrackBpm(String trackBpm) {
        this.trackBpm = trackBpm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release_track.track_genere
     *
     * @return the value of release_track.track_genere
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getTrackGenere() {
        return trackGenere;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release_track.track_genere
     *
     * @param trackGenere the value for release_track.track_genere
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setTrackGenere(String trackGenere) {
        this.trackGenere = trackGenere;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column release_track.tag
     *
     * @return the value of release_track.tag
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getTag() {
        return tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column release_track.tag
     *
     * @param tag the value for release_track.tag
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
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
        ReleaseTrack other = (ReleaseTrack) that;
        return (this.getIdReleaseTrack() == null ? other.getIdReleaseTrack() == null : this.getIdReleaseTrack().equals(other.getIdReleaseTrack()))
            && (this.getIdRelease() == null ? other.getIdRelease() == null : this.getIdRelease().equals(other.getIdRelease()))
            && (this.getIdGenre() == null ? other.getIdGenre() == null : this.getIdGenre().equals(other.getIdGenre()))
            && (this.getTrackNumber() == null ? other.getTrackNumber() == null : this.getTrackNumber().equals(other.getTrackNumber()))
            && (this.getTrackName() == null ? other.getTrackName() == null : this.getTrackName().equals(other.getTrackName()))
            && (this.getTrackTime() == null ? other.getTrackTime() == null : this.getTrackTime().equals(other.getTrackTime()))
            && (this.getTrackBpm() == null ? other.getTrackBpm() == null : this.getTrackBpm().equals(other.getTrackBpm()))
            && (this.getTrackGenere() == null ? other.getTrackGenere() == null : this.getTrackGenere().equals(other.getTrackGenere()))
            && (this.getTag() == null ? other.getTag() == null : this.getTag().equals(other.getTag()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIdReleaseTrack() == null) ? 0 : getIdReleaseTrack().hashCode());
        result = prime * result + ((getIdRelease() == null) ? 0 : getIdRelease().hashCode());
        result = prime * result + ((getIdGenre() == null) ? 0 : getIdGenre().hashCode());
        result = prime * result + ((getTrackNumber() == null) ? 0 : getTrackNumber().hashCode());
        result = prime * result + ((getTrackName() == null) ? 0 : getTrackName().hashCode());
        result = prime * result + ((getTrackTime() == null) ? 0 : getTrackTime().hashCode());
        result = prime * result + ((getTrackBpm() == null) ? 0 : getTrackBpm().hashCode());
        result = prime * result + ((getTrackGenere() == null) ? 0 : getTrackGenere().hashCode());
        result = prime * result + ((getTag() == null) ? 0 : getTag().hashCode());
        return result;
    }
}