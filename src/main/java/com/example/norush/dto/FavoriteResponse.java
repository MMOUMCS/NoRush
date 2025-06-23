import com.example.norush.domain.Favorite;
import java.time.LocalDateTime;

public record FavoriteResponse (
    String id, // 
    String userId, // username? 회원 id가 email인가?
    String name,
    String type,
    String targetId,
    LocalDateTime createdAt,
    String memo

) {
    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(
            favorite.getID(),
            favorite.getUserId(), // Email?
            favorite.getName(),
            favorite.getType(),
            favorite.getTargetId(),
            favorite.getCreatedAt(),
            favorite.getMemo()

        );
    }
}
