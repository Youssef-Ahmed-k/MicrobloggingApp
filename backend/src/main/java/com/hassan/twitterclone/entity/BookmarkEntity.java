package com.hassan.twitterclone.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@Table(name = "bookmark")
public class BookmarkEntity extends BaseSingleActionEntity{
}
