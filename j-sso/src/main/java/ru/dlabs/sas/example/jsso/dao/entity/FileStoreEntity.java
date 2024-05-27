package ru.dlabs.sas.example.jsso.dao.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import ru.dlabs.sas.example.jsso.dao.entity.common.VersionedBusinessEntity;
import ru.dlabs.sas.example.jsso.dao.type.StoreType;

@Getter
@Setter
@Entity
@Table(schema = "sso", name = "file_storage")
public class FileStoreEntity extends VersionedBusinessEntity<UUID> {

    @Id
    @Column(name = "file_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "store_type", nullable = false)
    private StoreType type;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "bucket", nullable = false)
    private String bucket;

}
