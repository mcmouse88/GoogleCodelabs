package com.mcmouse88.paging_advanced.data

import com.mcmouse88.paging_advanced.local.RepoEntity
import com.mcmouse88.paging_advanced.model.Repo

fun Repo.toEntity(): RepoEntity {
    return RepoEntity(
        id = id,
        name = name,
        fullName = fullName,
        description = description,
        url = url,
        stars = stars,
        forks = forks,
        language = language
    )
}

fun List<Repo>.toEntityList(): List<RepoEntity> {
    return this.map(Repo::toEntity)
}

fun RepoEntity.toRepo(): Repo {
    return Repo(
        id = id,
        name = name,
        fullName = fullName,
        description = description,
        url = url,
        stars = stars,
        forks = forks,
        language = language
    )
}