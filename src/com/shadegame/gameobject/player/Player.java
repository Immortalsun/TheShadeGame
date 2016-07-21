package com.shadegame.gameobject.player;

import com.shadegame.engine.collision.CollisionType;
import com.shadegame.gameobject.GameObject;
import com.shadegame.gameobject.animation.*;
import com.shadegame.gameobject.projectiles.MeleeAttack;
import com.shadegame.gameobject.projectiles.Projectile;
import processing.core.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Maashes on 3/30/2016.
 */
public class Player extends GameObject {

    private Projectile _currentProjectile;
    private boolean _charged,_charging;
    private int _health, _attackCounter;
    private ChargeState _chargeState;
    private HashMap<ChargeState,ArrayList<Animation>> _chargeAnimations;

    public Player(float x, float y, float objectWidth, float objectHeight, PApplet parent) {
        super(x, y, objectWidth, objectHeight, parent);

        _health = 1000;
        _chargeState = ChargeState.NONE;
        SetCollisionType(CollisionType.PLAYER);
        Animation[] animations = new Animation[5];
        animations[0] = new RunningAnimation(parent,"PlayerSprites/player1.png", "PlayerSprites/player1Reversed.png",3, 10);
        animations[1] = new JumpingAnimation(parent,"PlayerSprites/playerJump.png", "PlayerSprites/playerJumpReversed.png",2,10);
        animations[2] = new Animation(parent, "PlayerSprites/playerAttack.png", "PlayerSprites/playerAttackReversed.png",AnimationState.ATTACKING,5,10);
        animations[3] = new Animation(parent, "PlayerSprites/playerDamaged.png", "PlayerSprites/playerDamagedReversed.png",AnimationState.DAMAGED,4,10);
        animations[4] = new Animation(parent, "PlayerSprites/playerMeleeAttack.png", "PlayerSprites/playerMeleeAttackReversed.png",AnimationState.MELEEATTACK,6,4);
        BuildAnimator(animations);
        GenerateAnimationCollection();
    }

    public void GenerateAnimationCollection()
    {
        _chargeAnimations = new HashMap<>();
        ArrayList<Animation> fireChargeAnims = new ArrayList<>();
        fireChargeAnims.add(new Animation(GetParent(),"PlayerSprites/fireCharging.png","",AnimationState.CHARGING,8,4));
        fireChargeAnims.add(new Animation(GetParent(),"PlayerSprites/fireCharged.png","",AnimationState.CHARGED,3,10));
        _chargeAnimations.put(ChargeState.FIRE,fireChargeAnims);
        ArrayList<Animation> iceChargeAnims = new ArrayList<>();
        iceChargeAnims.add(new Animation(GetParent(),"PlayerSprites/iceCharging.png","",AnimationState.CHARGING,8,4));
        iceChargeAnims.add(new Animation(GetParent(),"PlayerSprites/iceCharged.png","",AnimationState.CHARGED,8,5));
        _chargeAnimations.put(ChargeState.ICE,iceChargeAnims);
    }

    public boolean GetHasCastProjectile()
    {
        return this.GetIsAttacking() && _currentProjectile != null;
    }

    public void ClearCurrentProjectile()
    {
        _currentProjectile = null;
    }

    public int GetHealth(){ return _health; }

    public void TakeDamage(int damage)
    {
        if(_health - damage > 0)
        {
            _health -= damage;
            SetIsDamaged(true);
        }
        else
        {
            _health = 0;
        }
    }

    public void Charge(int elementType)
    {
        switch(elementType)
        {
            case 1:
                _chargeState = ChargeState.FIRE;
                break;
            case 2:
                _chargeState = ChargeState.ICE;
                break;
            case 3:
                _chargeState = ChargeState.ELECTRIC;
                break;
            case 4:
                _chargeState = ChargeState.DARK;
                break;
            case 5:
                _chargeState = ChargeState.HOLY;
                break;
            case -1:
                _chargeState = ChargeState.NONE;
                break;
        }

        if(!_chargeState.equals(ChargeState.NONE))
        {
            _charging = true;
            SetIsAnimTriggered(true);
        }
    }

    @Override
    public PImage GetTriggeredAnimationFrame()
    {
        ArrayList<Animation> chargingAnimList = _chargeAnimations.get(_chargeState);
        if(_charging)
        {
            Animation chargingAnim = chargingAnimList.get(0);

            PImage frame = chargingAnim.GetNextFrame(false,0);

            if(chargingAnim.GetIsCompleted())
            {
                _charging = false;
                _charged = true;
            }
            return frame;
        }
        else if(_charged)
        {
            Animation chargedAnim = chargingAnimList.get(1);
            PImage frame = chargedAnim.GetNextFrame(false,0);

            return frame;
        }
        SetIsAnimTriggered(false);
        return null;
    }

    @Override
    public void SetStateBasedOnAnimationCompletion()
    {
        if(GetIsAttacking() && _attackCounter == 3)
        {
            _charged = false;
            _chargeState = ChargeState.NONE;
            _attackCounter = 0;
        }

        super.SetStateBasedOnAnimationCompletion();
    }

    public Projectile GetCurrentProjectile()
    {
        return _currentProjectile;
    }

    public void Move(PVector moveVector)
    {
        SetVelocity(moveVector);
    }

    public void Jump()
    {
        GetVelocity().y -= 12;
    }

    public void CastProjectile()
    {
        if(!_charged)
            return;

        if(_attackCounter < 3)
        {
            _attackCounter++;
        }
        SetIsAttacking(true, AttackType.BLAST);

        float startX;
        float startY;

        this.GetVelocity().x = 0;

        if(this.GetOrientation() > 0)
        {
            startX = this.GetLocation().x+this.GetWidth();
            startY = (this.GetLocation().y+this.GetHeight()/2)-20;
        }
        else
        {
            startX = this.GetLocation().x-this.GetWidth();
            startY = (this.GetLocation().y+this.GetHeight()/2)-20;
        }

        _currentProjectile = GetChargedProjectileAttack(startX,startY,this.GetOrientation());
        _currentProjectile.SetCollisionType(CollisionType.PLAYERPROJECTILE);
    }

    public void CastMeleeAttack()
    {
        if(!_charged)
            return;

        if(_attackCounter < 3)
        {
            _attackCounter++;
        }
        SetIsAttacking(true, AttackType.MELEE);

        float startX;
        float startY;

        this.GetVelocity().x = 0;

        if(this.GetOrientation() > 0)
        {
            startX = this.GetLocation().x+this.GetWidth();
            startY = (this.GetLocation().y+this.GetHeight()/2)-20;
        }
        else
        {
            startX = this.GetLocation().x-this.GetWidth();
            startY = (this.GetLocation().y+this.GetHeight()/2)-20;
        }

        _currentProjectile = GetChargedMeleeAttack(startX,startY,this.GetOrientation());
        _currentProjectile.SetCollisionType(CollisionType.PLAYERPROJECTILE);
    }

    private Projectile GetChargedProjectileAttack(float startX, float startY, int orientation)
    {
        switch(_chargeState)
        {
            case FIRE:
                Projectile fireball = new Projectile(startX,startY,32,32,10,5,400,orientation,GetParent());
                fireball.BuildProjectileAnimator("ProjectileSprites/fireball.png", "ProjectileSprites/fireballReversed.png",3, 5,
                        "ProjectileSprites/fireballRangeExplosion.png", "ProjectileSprites/fireballRangeExplosionReversed.png",5,5);
                return fireball;
            case ICE:
                Projectile iceSpike = new Projectile(startX,startY,32,32,10,5,400,orientation,GetParent());
                iceSpike.BuildProjectileAnimator("ProjectileSprites/iceSpike.png","",4,10,
                        "ProjectileSprites/iceSpikeExplosion.png","",4,5);
                return iceSpike;
        }
        return null;
    }

    private Projectile GetChargedMeleeAttack(float startX, float startY, int orientation)
    {
        switch(_chargeState)
        {
            case FIRE:
                Projectile fireSword = new MeleeAttack(startX, startY,orientation,GetParent());
                fireSword.BuildProjectileAnimator("ProjectileSprites/fireMeleeAttack.png","ProjectileSprites/fireMeleeAttackReversed.png",8,3,
                        "ProjectileSprites/fireballRangeExplosion.png","ProjectileSprites/fireballRangeExplosionReversed.png",5,5);
                return fireSword;
        }
        return null;
    }


}
