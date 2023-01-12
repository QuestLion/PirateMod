package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.Purgable;
import thePirate.characters.ThePirate;
import thePirate.actions.PurgeRemovablesAction;

import static thePirate.PirateMod.makeCardPath;

public class MakeshiftSpear extends AbstractDynamicCard implements Purgable {

    private boolean purge;

    // STAT DECLARATION
    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 9;
    private static final int UPGRADE_PLUS_DMG = 0;
    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(MakeshiftSpear.class.getSimpleName());
    public static final String IMG = makeCardPath(MakeshiftSpear.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public MakeshiftSpear() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new SFXAction("ATTACK_HEAVY"));
        this.addToBot(new VFXAction(p, new CleaveEffect(), 0.1F));
        this.addToBot(new DamageAllEnemiesAction(p, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
    }

    public void onSmith(){
        AbstractDungeon.effectsQueue.add(new PurgeCardEffect(this));
        AbstractDungeon.player.masterDeck.removeCard(this);
    }

    @Override
    public void update() {
        super.update();

        AbstractPlayer p = AbstractDungeon.player;
        if (p != null && this.upgraded && (p.masterDeck.contains(this) ||
                p.hand.contains(this) || p.discardPile.contains(this) || p.drawPile.contains(this) || p.exhaustPile.contains(this))){
            this.setPurge(true);
            this.addToBot(new PurgeRemovablesAction(this, true, true));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        upgradeName();
        upgradeDescription();
    }

    @Override
    public void setPurge(boolean purge) {
        this.purge = purge;

    }

    @Override
    public boolean getPurge() {
        return purge;
    }
}