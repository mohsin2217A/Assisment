package com.app.assisment.domain.use_case.get_university_listing

import com.app.assisment.domain.repository.ListingRepository
import javax.inject.Inject

class GetUniversityListingUseCase @Inject constructor(
    private val repository: ListingRepository
) {
    suspend operator fun invoke(hashMap: HashMap<String, String>) = repository.getData(hashMap)
}