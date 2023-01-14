package thePirate.cards.curses;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.Purgable;
import thePirate.cards.attacks.Overexert;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class Retreat extends AbstractDynamicCard implements Purgable {


    // TEXT DECLARATION

    public static final String ID = PirateMod.makeID(Retreat.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("Retreat.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private boolean purge;
    public boolean queuedForPurge;
    private boolean previewCards;


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.CURSE;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    public static final int BASE_VULNERABLE = 2;
    public static final int BASE_WEAK = 1;
    public static final int UPGRADED_VULNERABLE = -1;
    public static final int UPGRADED_WEAK = -1;

    // /STAT DECLARATION/


    public Retreat(boolean previewCards){
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = BASE_VULNERABLE;
        this.secondMagic = this.baseSecondMagic = BASE_WEAK;
        this.previewCards = previewCards;
        if(previewCards){
            this.cardsToPreview = new Overexert(false);
        }
    }

    public Retreat() {
        this(true);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, magicNumber, false)));
        if(secondMagic > 0){
            this.addToBot(new ApplyPowerAction(p, p, new WeakPower(p, secondMagic, false)));
        }

        AbstractCard overexert = new Overexert();
        if(this.upgraded){
            overexert.upgrade();
        }
        this.addToTop(new AddCardToDeckAction(overexert));
        this.setPurge(true);
//        this.addToBot(new PurgeRemovablesAction(this, true));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            if(previewCards){
                AbstractCard overexert = new Overexert(false);
                overexert.upgrade();
                cardsToPreview = overexert;
            }
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADED_VULNERABLE);
            upgradeSecondMagic(UPGRADED_WEAK);
            initializeDescription();
        }
    }

    @Override
    public void setPurge(boolean purge) {
        this.purge = purge;
    }

    @Override
    public boolean getPurge() {
        return this.purge;
    }

    @Override
    public boolean queuedForPurge() {
        return queuedForPurge;
    }
    @Override
    public void setQueuedForPurge(boolean queuedForPurge) {
        this.queuedForPurge = queuedForPurge;
    }
}
