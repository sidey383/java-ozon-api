package ru.sidey383.ozon.api.seller.objects.answer.fbo;

import java.util.List;

public record PostingFinancialData(
        PostingFinancialDataServices posting_services,
        String cluster_from,
        String cluster_to,
        List<FBOPostingFinancialDataProduct> products
) {
}
