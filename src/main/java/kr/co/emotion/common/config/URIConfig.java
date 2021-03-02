package kr.co.emotion.common.config;

/**
 * URI Class
 *
 * Class 내부에 있는 URI Class 를 위한 Class
 *
 * @since 2021-02-19 @author 류성재
 */
public abstract class URIConfig {

    public static final String uriCommunity = "/community";
    public static final String uriCommunityBoard = uriCommunity + "/board";
    public static final String uriCommunityBoardId = uriCommunity + "/board/{id}";

}
