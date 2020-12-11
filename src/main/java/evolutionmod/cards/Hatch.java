package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.InsectGene;
import evolutionmod.patches.AbstractCardEnum;

public class Hatch
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Hatch";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/InsectSkl.png";
    private static final int COST = 2;
    private static final int BLOCK_AMT = 4;
    private static final int DRONES_AMT = 2;
    private static final int UPGRADE_DRONES_AMT = 1;

    public Hatch() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = DRONES_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int drones = 1;
        if (upgraded || isPlayerInThisForm(InsectGene.ID)) {
            drones += 1;
        }
        addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p), drones));
        addToBot(new MakeTempCardInHandAction(new DroneGuard(), this.magicNumber));
        if (this.upgraded) {
            addToBot(new ChannelAction(new InsectGene()));
        } else {
            formEffect(InsectGene.ID);
        }
    }

//    @Override
//    public void applyPowers() {
//        calculateDroneAmount();
//        super.applyPowers();
//        this.isMagicNumberModified = this.magicNumber != DRONES_AMT + (this.upgraded ? UPGRADE_DRONES_AMT : 0);
//    }
//
//    @Override
//    public void calculateCardDamage(AbstractMonster mo) {
//        calculateDroneAmount();
//        super.calculateCardDamage(mo);
//        this.isMagicNumberModified = this.magicNumber != DRONES_AMT + (this.upgraded ? UPGRADE_DRONES_AMT : 0);
//    }
//
//    private void calculateDroneAmount() {
//        this.magicNumber = this.baseMagicNumber = DRONES_AMT;
//        if (this.upgraded || BaseEvoCard.isPlayerInThisForm(InsectGene.ID)) {
//            this.magicNumber += UPGRADE_DRONES_AMT;
//            this.baseMagicNumber += UPGRADE_DRONES_AMT;
//        }
//    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeMagicNumber(UPGRADE_DRONES_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}