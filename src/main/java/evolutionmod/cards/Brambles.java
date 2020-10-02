package evolutionmod.cards;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.BramblesPower;

import java.util.ArrayList;
import java.util.List;

public class Brambles
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Brambles";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/PlantForm.png";
    private static final int COST = 2;
    private static final int BLOCK_AMT = 12;
    private static final int UPGRADE_BLOCK_AMT = 4;
    private static final int THORN_AMT = 4;
    private static final int UPGRADE_THORN_AMT = 2;

    public Brambles() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = THORN_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        if (AbstractGene.isPlayerInThisForm(PlantGene.ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    p, p, new BramblesPower(p, this.magicNumber), this.magicNumber));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(new PlantGene()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Brambles();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
            this.upgradeMagicNumber(UPGRADE_THORN_AMT);
        }
    }
}