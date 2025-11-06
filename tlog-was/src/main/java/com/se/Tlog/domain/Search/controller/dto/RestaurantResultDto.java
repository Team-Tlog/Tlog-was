package com.se.Tlog.domain.Search.controller.dto;

import com.se.Tlog.domain.Search.repository.dto.KakaoLocalSearchRes;
import com.se.Tlog.domain.Search.repository.dto.NaverBlog;
import com.se.Tlog.domain.Search.repository.dto.NaverImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class RestaurantResultDto {
    @Schema(example = "숙이네 닭백숙")
    private String place_name;
    @Schema(example = "음식점 > 한식 > 백숙점")
    private String category_name;
    @Schema(example = "02-0011-1100")
    private String phone;
    @Schema(example = "티로그시 지번주소 511-1")
    private String address_name;
    @Schema(example = "티로그시 도로명주소 영남대 5길 12")
    private String road_address_name;
    @Schema(example = "127.0011011101010")
    private String longitude;
    @Schema(example = "37.00110111010100")
    private String latitude;
    @Schema(example = "http://place.map.kakao.com/카카오맵-주소")
    private String place_url;
    @Schema(example = "거리 (단위:m)")
    private String distance;

    @Data
    @AllArgsConstructor
    public static class Image {
        @Schema(example = "이미지가 검색된 문서의 이름")
        private String title;
        @Schema(example = "이미지 URL")
        private String link;
        @Schema(example = "썸네일 URL")
        private String thumbnail;
        @Schema(example = "세로크기 픽셀")
        private String sizeheight;
        @Schema(example = "가로크기 픽셀")
        private String sizewidth;

        static Image from(NaverImage image) {
            return new Image(
                    image.getTitle(),
                    image.getLink(),
                    image.getThumbnail(),
                    image.getSizeheight(),
                    image.getSizewidth()
            );
        }
    }
    private List<Image> images;

    @Data
    @AllArgsConstructor
    public static class Blog {
        @Schema(example = "블로그 포스트 제목")
        private String title;
        @Schema(example = "검색된 포스트 링크")
        private String link;
        @Schema(example = "검색된 포스트의 요약")
        private String description;
        @Schema(example = "글을 쓴 블로그의 이름")
        private String bloggername;
        @Schema(example = "포스트 작성 날짜")
        private LocalDate postdate;

        static Blog from(NaverBlog blog) {
            return new Blog(
                    blog.getTitle(),
                    blog.getLink(),
                    blog.getDescription(),
                    blog.getBloggername(),
                    blog.getPostdate()
            );
        }
    }
    private List<Blog> blogs;

    public static RestaurantResultDto from(KakaoLocalSearchRes.Document restaurant, List<NaverImage> images, List<NaverBlog> blogs) {
        return new RestaurantResultDto(
                restaurant.getPlace_name(),
                restaurant.getCategory_name(),
                restaurant.getPhone(),
                restaurant.getAddress_name(),
                restaurant.getRoad_address_name(),
                restaurant.getX(),
                restaurant.getY(),
                restaurant.getPlace_url(),
                restaurant.getDistance(),
                images.stream().map(Image::from).toList(),
                blogs.stream().map(Blog::from).toList()
        );
    }
}
