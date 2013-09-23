package it.fivano.symusic.backend.model;

import java.io.Serializable;

public class User implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.id_user
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private Long idUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.password
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String password;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.id_user_preference
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private Long idUserPreference;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.user_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String userName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.mail
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private String mail;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.abilitato
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private byte[] abilitato;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table user
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.id_user
     *
     * @return the value of user.id_user
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public Long getIdUser() {
        return idUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.id_user
     *
     * @param idUser the value for user.id_user
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.password
     *
     * @return the value of user.password
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.password
     *
     * @param password the value for user.password
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.id_user_preference
     *
     * @return the value of user.id_user_preference
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public Long getIdUserPreference() {
        return idUserPreference;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.id_user_preference
     *
     * @param idUserPreference the value for user.id_user_preference
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setIdUserPreference(Long idUserPreference) {
        this.idUserPreference = idUserPreference;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.user_name
     *
     * @return the value of user.user_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.user_name
     *
     * @param userName the value for user.user_name
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.mail
     *
     * @return the value of user.mail
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public String getMail() {
        return mail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.mail
     *
     * @param mail the value for user.mail
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.abilitato
     *
     * @return the value of user.abilitato
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public byte[] getAbilitato() {
        return abilitato;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.abilitato
     *
     * @param abilitato the value for user.abilitato
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    public void setAbilitato(byte[] abilitato) {
        this.abilitato = abilitato;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
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
        User other = (User) that;
        return (this.getIdUser() == null ? other.getIdUser() == null : this.getIdUser().equals(other.getIdUser()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getIdUserPreference() == null ? other.getIdUserPreference() == null : this.getIdUserPreference().equals(other.getIdUserPreference()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getMail() == null ? other.getMail() == null : this.getMail().equals(other.getMail()))
            && (this.getAbilitato() == null ? other.getAbilitato() == null : this.getAbilitato().equals(other.getAbilitato()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIdUser() == null) ? 0 : getIdUser().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getIdUserPreference() == null) ? 0 : getIdUserPreference().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getMail() == null) ? 0 : getMail().hashCode());
        result = prime * result + ((getAbilitato() == null) ? 0 : getAbilitato().hashCode());
        return result;
    }
}