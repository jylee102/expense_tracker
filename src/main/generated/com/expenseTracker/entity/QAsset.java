package com.expenseTracker.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAsset is a Querydsl query type for Asset
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAsset extends EntityPathBase<Asset> {

    private static final long serialVersionUID = -1687372410L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAsset asset = new QAsset("asset");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> position = createNumber("position", Integer.class);

    public QAsset(String variable) {
        this(Asset.class, forVariable(variable), INITS);
    }

    public QAsset(Path<? extends Asset> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAsset(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAsset(PathMetadata metadata, PathInits inits) {
        this(Asset.class, metadata, inits);
    }

    public QAsset(Class<? extends Asset> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

