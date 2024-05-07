package com.hassan.twitterclone.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@Table(name = "like_action")
public class LikeEntity extends BaseSingleActionEntity{


}
