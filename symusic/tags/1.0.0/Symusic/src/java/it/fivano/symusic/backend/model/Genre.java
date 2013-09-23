package it.fivano.symusic.backend.model;

import java.io.Serializable;

public class Genre implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column genre.id_genre
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private Integer idGenre;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column genre.name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table genre
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column genre.id_genre
     *
     * @return the value of genre.id_genre
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public Integer getIdGenre() {
        return idGenre;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column genre.id_genre
     *
     * @param idGenre the value for genre.id_genre
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setIdGenre(Integer idGenre) {
        this.idGenre = idGenre;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column genre.name
     *
     * @return the value of genre.name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column genre.name
     *
     * @param name the value for genre.name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table genre
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
        Genre other = (Genre) that;
        return (this.getIdGenre() == null ? other.getIdGenre() == null : this.getIdGenre().equals(other.getIdGenre()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table genre
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIdGenre() == null) ? 0 : getIdGenre().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }
}