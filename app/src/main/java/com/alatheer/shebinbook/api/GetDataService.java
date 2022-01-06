package com.alatheer.shebinbook.api;



import com.alatheer.shebinbook.allproducts.ProductModel;
import com.alatheer.shebinbook.authentication.cities.CityModel;
import com.alatheer.shebinbook.authentication.favorite.FavoriteStoreModel;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.comments.Comment;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.comments.ReplyModel;
import com.alatheer.shebinbook.home.category.CategoryModel;
import com.alatheer.shebinbook.home.slider.SliderModel;
import com.alatheer.shebinbook.message.MessageModel;
import com.alatheer.shebinbook.message.reply.ReplayModel;
import com.alatheer.shebinbook.posts.PostData;
import com.alatheer.shebinbook.posts.PostModel;
import com.alatheer.shebinbook.products.GalleryModel;
import com.alatheer.shebinbook.products.rating.RatingModel;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.stores.StoreModel;
import com.alatheer.shebinbook.trader.images.ImagesData;

import java.security.MessageDigest;
import java.util.List;

import butterknife.OnFocusChange;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService{
/*1*/
    @GET("api/member/cities")
    Call<CityModel>get_all_cities(@Query("from_id")String from_id);
    /*2*/
    @FormUrlEncoded
    @POST("api/member/register")
    Call<LoginModel> add_user(@Field("name")String name,
                              @Field("last_name")String last_name,
                              @Field("phone")String phone,
                              @Field("password")String password,
                              @Field("gender")String gender,
                              @Field("city_id")String city_id);
    /*3*/
    @Multipart
    @POST("api/member/register")
    Call<LoginModel> add_user_with_img(@Part("name") RequestBody name,
                                       @Part("last_name")RequestBody last_name,
                                       @Part("phone")RequestBody phone,
                                       @Part("password")RequestBody password,
                                       @Part("gender")RequestBody gender,
                                       @Part("city_id")RequestBody city_id,
                                       @Part MultipartBody.Part user_img);
    /*4*/
    @FormUrlEncoded
    @POST("api/member/login")
    Call<LoginModel> login_user(@Field("phone")String phone,@Field("password")String password);

    /*5*/
    @FormUrlEncoded
    @POST("api/member/get_user_data")
    Call<ProfileData> get_user_data(@Field("user_id")String user_id);

    /*6*/
    @Multipart
    @POST("api/member/update_user")
    Call<ProfileData> update_user_with_img(@Part("user_id_fk")RequestBody user_id_fk,
                                          @Part("name") RequestBody name,
                                          @Part("last_name")RequestBody last_name,
                                          @Part("phone")RequestBody phone,
                                          @Part("password")RequestBody password,
                                          @Part("gender")RequestBody gender,
                                          @Part("city_id")RequestBody city_id,
                                          @Part MultipartBody.Part user_img);

    /*7*/
    @FormUrlEncoded
    @POST("api/member/update_user")
    Call<ProfileData> update_user_without_img(@Field("user_id_fk") String user_id_fk,
                                          @Field("name") String name,
                                          @Field("last_name") String last_name,
                                          @Field("phone") String phone,
                                          @Field("password") String password,
                                          @Field("gender") String gender,
                                          @Field("city_id") String city_id);

    /*8*/
    @FormUrlEncoded
    @POST("api/member/get_offers_screens")
    Call<SliderModel> get_ads(@Field("gender")String gender,@Field("screen")Integer screen);

    /*9*/
    @FormUrlEncoded
    @POST("api/member/get_offers_screens")
    Call<SliderModel> get_ads_in_store(@Field("gender")String gender,@Field("screen")Integer screen,
                                       @Field("trader_id_fk")String trader_id_fk);

    /*10*/
    @GET("api/member/categories")
    Call<CategoryModel> get_categories();

    /*11*/
    @FormUrlEncoded
    @POST("api/member/insert_post")
    Call<PostModel> insert_post(@Field("gender")String gender,
                                @Field("user_id_fk")String user_id_fk,
                                @Field("post")String post);
    /*12*/
    @Multipart
    @POST("api/member/insert_post")
    Call<PostModel> insert_post_with_img(@Part("gender")RequestBody gender,
                                         @Part("user_id_fk")RequestBody user_id_fk,
                                         @Part("post")RequestBody post,
                                         @Part MultipartBody.Part img);

    /*13*/
    @FormUrlEncoded
    @POST("api/member/get_all_posts")
    Call<PostData> get_all_posts(@Field("gender")String gender,@Field("page")Integer page,
                                 @Field("user_id_fk")String user_id_fk);

    /*14*/
    @FormUrlEncoded
    @POST("api/member/add_comment_to_post")
    Call<CommentModel> add_comment(@Field("comment_user_id_fk")String comment_user_id_fk,
                                   @Field("post_user_id_fk")String post_user_id_fk,
                                   @Field("post_id_fk")String post_id_fk,
                                   @Field("comment")String comment);

    /*15*/
    @Multipart
    @POST("api/member/add_comment_to_post")
    Call<CommentModel> add_comment_with_img(@Part("comment_user_id_fk")RequestBody comment_user_id_fk,
                                   @Part("post_user_id_fk")RequestBody post_user_id_fk,
                                   @Part("post_id_fk")RequestBody post_id_fk,
                                   @Part("comment")RequestBody comment,
                                            @Part MultipartBody.Part img);
    /*16*/
    @FormUrlEncoded
    @POST("api/member/get_post_comment")
    Call<List<Comment>> get_comments(@Field("post_id_fk")String post_id_fk);

    /*17*/
    @FormUrlEncoded
    @POST("api/member/add_replay_comment")
    Call<CommentModel> add_reply(@Field("post_id_fk")String post_id_fk,
                                   @Field("post_user_id_fk")String post_user_id_fk,
                                   @Field("comment_id_fk")String comment_id_fk,
                                   @Field("comment_user_id_fk")String comment_user_id_fk,
                                   @Field("user_id_fk")String user_id_fk,
                                   @Field("replay_message")String replay_message);
    /*18*/
    @FormUrlEncoded
    @POST("api/member/get_stores")
    Call<StoreModel> get_stores(@Field("user_id_fk")String user_id_fk,@Field("tsnef") String tsnef,@Field("page")Integer page);

    /*19*/
    @FormUrlEncoded
    @POST("api/member/add_like_store")
    Call<CommentModel> add_to_fav(@Field("user_id_fk")String user_id_fk,@Field("store_id_fk")String store_id_fk);

    /*20*/
    @FormUrlEncoded
    @POST("api/member/delete_like")
    Call<CommentModel> delete_from_fav(@Field("user_id_fk")String user_id_fk,@Field("store_id_fk")String store_id_fk);

    /*21*/
    @FormUrlEncoded
    @POST("api/member/get_like_store")
    Call<FavoriteStoreModel> get_fav_stores(@Field("user_id_fk")String user_id_fk,@Field("page")Integer page);

    /*22*/
    @FormUrlEncoded
    @POST("api/member/get_all_alboum_by_store")
    Call<GalleryModel> get_galleries(@Field("store_id_fk")String store_id_fk);

    /*23*/
    @FormUrlEncoded
    @POST("api/member/get_all_progucts_from_alboum")
    Call<ProductModel> get_products(@Field("alboum_id_fk")String alboum_id_fk);

    /*24*/
    @FormUrlEncoded
    @POST("api/member/send_message_to_store")
    Call<CommentModel> send_message(@Field("user_id_fk")String user_id_fk,
                                    @Field("product_id_fk")String product_id_fk,
                                    @Field("trader_id_fk")String trader_id_fk,
                                    @Field("store_id_fk")String store_id_fk,
                                    @Field("message")String message);

    @FormUrlEncoded
    @POST("api/member/send_message_to_store")
    Call<CommentModel> send_offer_message(@Field("user_id_fk")String user_id_fk,
                                          @Field("offer_id_fk")String offer_id_fk,
                                          @Field("trader_id_fk")String trader_id_fk,
                                          @Field("store_id_fk")String store_id_fk,
                                          @Field("message")String message);
    /*25*/
    @FormUrlEncoded
    @POST("api/member/get_stores_by_trader")
    Call<StoreModel> get_trader_store(@Field("trader_id_fk")String trader_id_fk);
    @FormUrlEncoded
    @POST("api/member/get_stores_by_trader")
    Call<StoreModel> get_trader_store_by_user(@Field("trader_id_fk")String trader_id_fk,@Field("user_id_fk")String user_id_fk);

    /*26*/
    @FormUrlEncoded
    @POST("api/member/add_alboum")
    Call<CommentModel> add_alboum(@Field("trader_id_fk")String trader_id_fk,@Field("store_id_fk")String store_id_fk,@Field("title")String title);

    /*27*/
    @FormUrlEncoded
    @POST("api/member/update_alboum")
    Call<CommentModel> update_alboum(@Field("trader_id_fk")String trader_id_fk,@Field("store_id_fk")String store_id_fk,@Field("title")String title,@Field("row_id")String row_id);

    /*28*/
    @Multipart
    @POST("api/member/add_product_alboum")
    Call<CommentModel> add_product_to_alboum(@Part("trader_id_fk")RequestBody trader_id_fk,
                                             @Part("store_id_fk")RequestBody store_id_fk,
                                             @Part("alboum_id_fk")RequestBody alboum_id_fk,
                                             @Part("title")RequestBody title,
                                             @Part("price")RequestBody price,
                                             @Part("details")RequestBody details,
                                             @Part MultipartBody.Part product_img);

    /*29*/
    @Multipart
    @POST("api/member/update_product_alboum")
    Call<CommentModel> update_product_to_alboum_with_img(@Part("row_id")RequestBody row_id,
                                                @Part("trader_id_fk")RequestBody trader_id_fk,
                                                @Part("store_id_fk")RequestBody store_id_fk,
                                                @Part("alboum_id_fk")RequestBody alboum_id_fk,
                                                @Part("title")RequestBody title,
                                                @Part("price")RequestBody price,
                                                @Part("details")RequestBody details,
                                                @Part MultipartBody.Part product_img);
    /*30*/
    @FormUrlEncoded
    @POST("api/member/update_product_alboum")
    Call<CommentModel> update_product_to_alboum_without_img(@Field("row_id") String row_id,
                                                         @Field("trader_id_fk") String trader_id_fk,
                                                         @Field("store_id_fk") String store_id_fk,
                                                         @Field("alboum_id_fk") String alboum_id_fk,
                                                         @Field("title") String title,
                                                         @Field("price") String price,
                                                         @Field("details") String details);

    /*31*/
    @FormUrlEncoded
    @POST("api/member/delete_product_alboum")
    Call<CommentModel> delete_product_from_alboum(@Field("row_id")String row_id);

    /*32*/
    @Multipart
    @POST("api/member/add_offer")
    Call<CommentModel> add_offer(@Part("title")RequestBody title,
                                 @Part("gender")RequestBody type,
                                 @Part("trader_id_fk")RequestBody trader_id_fk,
                                 @Part("from_date")RequestBody from_date,
                                 @Part("to_date")RequestBody to_date,
                                 @Part("price_before_offer")RequestBody price_before_offer,
                                 @Part("price_after_offer")RequestBody price_after_offer,
                                 @Part("description")RequestBody description,
                                 @Part MultipartBody.Part img);
    /*33*/
    @Multipart
    @POST("api/member/update_offers")
    Call<CommentModel> update_offer_with_img(@Part("row_id")RequestBody row_id,
                                    @Part("title")RequestBody title,
                                    @Part("gender")RequestBody type,
                                    @Part("trader_id_fk")RequestBody trader_id_fk,
                                    @Part("from_date")RequestBody from_date,
                                    @Part("to_date")RequestBody to_date,
                                    @Part("price_before_offer")RequestBody price_before_offer,
                                    @Part("price_after_offer")RequestBody price_after_offer,
                                             @Part("description")RequestBody description,
                                    @Part MultipartBody.Part img);

    /*34*/
    @FormUrlEncoded
    @POST("api/member/update_offers")
    Call<CommentModel> update_offer_without_img(@Field("row_id") String row_id,
                                             @Field("title") String title,
                                             @Field("gender") String type,
                                             @Field("trader_id_fk") String trader_id_fk,
                                             @Field("from_date") String from_date,
                                             @Field("to_date") String to_date,
                                             @Field("price_before_offer") String price_before_offer,
                                             @Field("price_after_offer") String price_after_offer,
                                                @Field("description")String description);

    /*35*/
    @FormUrlEncoded
    @POST("api/member/delete_offer")
    Call<CommentModel> delete_offer(@Field("row_id")Integer row_id);

    /*36*/
    @FormUrlEncoded
    @POST("api/member/add_rate")
    Call<CommentModel> add_rate(@Field("user_id_fk")String user_id_fk,
                                @Field("rate")String rate,
    /*37*/                      @Field("store_id_fk")String store_id_fk,
                                @Field("desc")String desc);
    @FormUrlEncoded
    @POST("api/member/add_click")
    Call<CommentModel> add_click(@Field("user_id_fk")String user_id_fk,
    /*38*/                             @Field("offer_id_fk")String offer_id_fk);
    @FormUrlEncoded
    @POST("api/member/get_user_message")
    Call<MessageModel> get_messages(@Field("trader_id_fk")String trader_id_fk,
                                    @Field("page")Integer page);
    //39
    @FormUrlEncoded
    @POST("api/member/get_user_message")
    Call<MessageModel> get_user_messages(@Field("user_id_fk")String user_id_fk,
                                         @Field("page")Integer page);
    //40
    @FormUrlEncoded
    @POST("api/member/send_replay_to_message")
    Call<CommentModel> send_replay_to_message(@Field("user_id_fk")Integer user_id_fk,
                                 @Field("replay")String replay,
                                 @Field("store_id_fk")Integer store_id_fk,
                                 @Field("product_id_fk")Integer product_id_fk,
                                 @Field("trader_id_fk")Integer trader_id_fk,
                                 @Field("message_id_fk")Integer message_id_fk,
                                              @Field("type")Integer type);
    //41
    @FormUrlEncoded
    @POST("api/member/get_replay_message")
    Call<ReplayModel> get_replay_message(@Field("message_id_fk") Integer message_id_fk);
    //42
    @FormUrlEncoded
    @POST("api/member/get_store_rating")
    Call<RatingModel> get_rates(@Field("store_id_fk")String store_id_fk);
    //43
    @FormUrlEncoded
    @POST("api/member/search_store")
    Call<StoreModel> search_store(@Field("store_name")String store_name);
    //44
    @FormUrlEncoded
    @POST("api/member/update_store")
    Call<CommentModel> update_store(@Field("description")String description,
                                    @Field("mini_description")String mini_description,
                                    @Field("store_whats")String store_whats,
                                    @Field("facebook")String facebook,
                                    @Field("appointments_work")String appointments_work,
                                    @Field("store_address")String store_address,
                                    @Field("store_mobile")String store_mobile,
                                    @Field("row_id")String store_id);
    //45
    @Multipart
    @POST("api/member/add_gallery")
    Call<CommentModel> add_gallery(@Part("title")RequestBody title,
                                    @Part("trader_id_fk")RequestBody trader_id_fk,
                                    @Part List<MultipartBody.Part> images,
                                   @Part("store_id_fk")RequestBody store_id_fk);
    //46
    @FormUrlEncoded
    @POST("api/member/get_gallery_by_store")
    Call<ImagesData>  get_gallery(@Field("trader_id_fk")String trader_id_fk);

    @FormUrlEncoded
    @POST("api/member/add_like_to_post")
    Call<CommentModel> add_post_to_fav(@Field("post_id_fk")String post_id_fk,@Field("user_id_fk")String user_id_fk);

    @FormUrlEncoded
    @POST("api/member/delete_like_to_post")
    Call<CommentModel> delete_post_from_fav(@Field("post_id_fk")String post_id_fk,@Field("user_id_fk")String user_id_fk);

    @Multipart
    @POST("api/member/update_store")
    Call<CommentModel> update_store_with_img(@Part("row_id")RequestBody row_id,
                                             @Part("description")RequestBody description,
                                             @Part("appointments_work")RequestBody appointments_work,
                                             @Part("facebook")RequestBody facebook,
                                             @Part("store_whats")RequestBody store_whats,
                                             @Part("mini_description")RequestBody mini_description,
                                             @Part("store_address")RequestBody store_address,
                                             @Part("store_mobile")RequestBody store_mobile,
                                             @Part MultipartBody.Part logo,
                                             @Part("Governorate")RequestBody Governorate,
                                             @Part("City")RequestBody City,
                                             @Part("long_map")RequestBody long_map,
                                             @Part("lat_map")RequestBody lat_map,
                                             @Part("offers_words")RequestBody offers_words,
                                             @Part("offers_products")RequestBody offers_products,
                                             @Part("instagram")RequestBody instagram,
                                             @Part("store_name")RequestBody store_name);
    @FormUrlEncoded
    @POST("api/member/update_store")
    Call<CommentModel> update_store_without_img(@Field("row_id") String row_id,
                                             @Field("description") String description,
                                             @Field("appointments_work") String appointments_work,
                                             @Field("facebook") String facebook,
                                             @Field("store_whats") String store_whats,
                                             @Field("mini_description") String mini_description,
                                             @Field("store_address") String store_address,
                                             @Field("store_mobile") String store_mobile,
                                             @Field("Governorate") String Governorate,
                                             @Field("City") String  City,
                                             @Field("long_map") String  long_map,
                                             @Field("lat_map") String lat_map,
                                             @Field("offers_words") String offers_words,
                                             @Field("offers_products") String offers_products,
                                             @Field("instagram") String instagram, @Field("store_name")String store_name);
    @FormUrlEncoded
    @POST("api/member/delete_rating")
    Call<CommentModel> delete_rating(@Field("row_id")Integer row_id);

    @FormUrlEncoded
    @POST("api/member/delete_gallery")
    Call<CommentModel> delete_from_gallery(@Field("row_id")Integer row_id);

    @FormUrlEncoded
    @POST("api/member/delete_posts")
    Call<CommentModel> delete_post(@Field("row_id")Integer row_id);

}

