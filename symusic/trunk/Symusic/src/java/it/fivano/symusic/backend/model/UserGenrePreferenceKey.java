package it.fivano.symusic.backend.model;

import java.io.Serializable;

public class UserGenrePreferenceKey implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_genre_preference.id_genre
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private Integer idGenre;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_genre_preference.id_user
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private Long idUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table user_genre_preference
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_genre_preference.id_genre
     *
     * @return the value of user_genre_preference.id_genre
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public Integer getIdGenre() {
        return idGenre;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_genre_preference.id_genre
     *
     * @param idGenre the value for user_genre_preference.id_genre
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setIdGenre(Integer idGenre) {
        this.idGenre = idGenre;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_genre_preference.id_user
     *
     * @return the value of user_genre_preference.id_user
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public Long getIdUser() {
        return idUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_genre_preference.id_user
     *
     * @param idUser the value for user_genre_preference.id_user
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_genre_preference
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
        UserGenrePreferenceKey other = (UserGenrePreferenceKey) that;
        return (this.getIdGenre() == null ? other.getIdGenre() == null : this.getIdGenre().equals(other.getIdGenre()))
            && (this.getIdUser() == null ? other.getIdUser() == null : this.getIdUser().equals(other.getIdUser()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_genre_preference
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIdGenre() == null) ? 0 : getIdGenre().hashCode());
        result = prime * result + ((getIdUser() == null) ? 0 : getIdUser().hashCode());
        return result;
    }
}