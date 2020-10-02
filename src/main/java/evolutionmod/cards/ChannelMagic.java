package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.PotencyPower;
import evolutionmod.powers.RemovePotencyPower;

public class ChannelMagic
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:ChannelMagic";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int POTENCY_AMT = 1;
    private static final int FORM_POTENCY_AMT = 1;
    private static final int UPGRADE_FORM_POTENCY_AMT = 1;

    public ChannelMagic() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = FORM_POTENCY_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int potency = POTENCY_AMT;
        if (AbstractGene.isPlayerInThisForm(LymeanGene.ID)
                || AbstractGene.isPlayerInThisForm(InsectGene.ID)
                || AbstractGene.isPlayerInThisForm(LavafolkGene.ID)
        ) {
            potency += this.magicNumber;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                p, p, new PotencyPower(p, potency)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                p, p, new RemovePotencyPower(p, potency)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FORM_POTENCY_AMT);
        }
    }
}