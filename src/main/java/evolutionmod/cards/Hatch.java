package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.PotencyPower;
import evolutionmod.powers.RemovePotencyPower;

public class Hatch
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Hatch";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/InsectSkl.png";
    private static final int COST = 2;
    private static final int DRONES_AMT = 3;
    private static final int UPGRADE_DRONES_AMT = 1;
    private static final int POTENCY_AMT = 1;

    public Hatch() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DRONES_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p), this.magicNumber));
        if (AbstractGene.isPlayerInThisForm(InsectGene.ID)) {
            addToBot(new ApplyPowerAction(p, p, new PotencyPower(p, POTENCY_AMT)));
            addToBot(new ApplyPowerAction(p, p, new RemovePotencyPower(p, POTENCY_AMT)));
        } else {
            addToBot(new ChannelAction(new InsectGene()));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DRONES_AMT);
        }
    }
}