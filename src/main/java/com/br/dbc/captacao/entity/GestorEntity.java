package com.br.dbc.captacao.entity;

import com.br.dbc.captacao.enums.Genero;
import com.br.dbc.captacao.enums.TipoMarcacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "GESTOR")
public class GestorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GESTOR")
    @SequenceGenerator(name = "SEQ_GESTOR", sequenceName = "SEQ_GESTOR", allocationSize = 1)
    @Column(name = "ID_GESTOR")
    private Integer idGestor;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "ativo")
    @Enumerated(EnumType.STRING)
    private TipoMarcacao ativo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cargo")
    private CargoEntity cargoEntity;

    @Column(name = "genero")
    @Enumerated(EnumType.STRING)
    private Genero genero;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<CargoEntity> lista = new ArrayList();
//        lista.add(cargoEntity);
//        return lista;
//    }
//
//    @Override
//    public String getPassword() {
//        return senha;
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return getAtivo() == TipoMarcacao.T;
//    }

}
