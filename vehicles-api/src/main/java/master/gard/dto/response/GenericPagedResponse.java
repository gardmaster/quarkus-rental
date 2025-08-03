package master.gard.dto.response;

import java.util.List;

public record GenericPagedResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int currentPage,
        int pageSize
) {
    public static <T> GenericPagedResponse<T> of(List<T> content, long totalElements, int currentPage, int totalPages, int pageSize) {
        return new GenericPagedResponse<>(
                content,
                totalElements,
                totalPages,
                currentPage,
                pageSize
        );
    }

}
