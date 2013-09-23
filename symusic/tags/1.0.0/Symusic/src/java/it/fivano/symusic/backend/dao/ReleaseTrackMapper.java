package it.fivano.symusic.backend.dao;

import it.fivano.symusic.backend.model.ReleaseTrack;
import it.fivano.symusic.backend.model.ReleaseTrackExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReleaseTrackMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int countByExample(ReleaseTrackExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int deleteByExample(ReleaseTrackExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int deleteByPrimaryKey(Long idReleaseTrack);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int insert(ReleaseTrack record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int insertSelective(ReleaseTrack record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    List<ReleaseTrack> selectByExample(ReleaseTrackExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    ReleaseTrack selectByPrimaryKey(Long idReleaseTrack);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int updateByExampleSelective(@Param("record") ReleaseTrack record, @Param("example") ReleaseTrackExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int updateByExample(@Param("record") ReleaseTrack record, @Param("example") ReleaseTrackExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int updateByPrimaryKeySelective(ReleaseTrack record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table release_track
     *
     * @mbggenerated Sun Sep 01 01:31:17 CEST 2013
     */
    int updateByPrimaryKey(ReleaseTrack record);
}