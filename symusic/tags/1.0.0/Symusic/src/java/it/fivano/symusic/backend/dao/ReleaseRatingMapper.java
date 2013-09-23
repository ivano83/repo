package it.fivano.symusic.backend.dao;

import it.fivano.symusic.backend.model.ReleaseRating;
import it.fivano.symusic.backend.model.ReleaseRatingExample;
import it.fivano.symusic.backend.model.ReleaseRatingKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReleaseRatingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_rating
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int countByExample(ReleaseRatingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_rating
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int deleteByExample(ReleaseRatingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_rating
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int deleteByPrimaryKey(ReleaseRatingKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_rating
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int insert(ReleaseRating record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_rating
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int insertSelective(ReleaseRating record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_rating
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    List<ReleaseRating> selectByExample(ReleaseRatingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_rating
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    ReleaseRating selectByPrimaryKey(ReleaseRatingKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_rating
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int updateByExampleSelective(@Param("record") ReleaseRating record, @Param("example") ReleaseRatingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_rating
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int updateByExample(@Param("record") ReleaseRating record, @Param("example") ReleaseRatingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_rating
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int updateByPrimaryKeySelective(ReleaseRating record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_rating
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int updateByPrimaryKey(ReleaseRating record);
}